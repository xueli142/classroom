package org.example.classroom.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.user.StudentMapper;
import org.example.classroom.dao.user.UserMapper;
import org.example.classroom.transfer.dto.insertDto.StudentDto;
import org.example.classroom.transfer.dto.selectDto.StudentSelectDto;
import org.example.classroom.transfer.dto.simpleVo.StudentVo;
import org.example.classroom.model.user.Student;
import org.example.classroom.model.user.User;
import org.example.classroom.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Resource
    StudentMapper studentMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    PasswordUtil passwordUtil;
    @Resource
    UserService userService;

    //同时插入，一起成功，一起回滚
    @Transactional
    public Boolean insertStudent(StudentDto dto){
        User user = new User();
        Student student = new Student();
        String userId = UUID.randomUUID().toString();

        user.setUid(dto.getUid());
        user.setName(dto.getName());
        user.setPassword(passwordUtil.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRole("STUDENT");
        user.setUserId(userId);
        user.setBesides(dto.getBesides());
        user.setIsActive(dto.getIsActive());

        student.setGroups(dto.getGroups());
        student.setUserId(userId);
        student.setUuid(dto.getUuid());
        student.setGrade(dto.getGrade());
        student.setSchool(dto.getSchool());
        student.setMajor(dto.getMajor());
        student.setCollege(dto.getCollege());
        student.setBesides(dto.getBesides());

        int insert1 = userMapper.insert(user);
        int insert2 = studentMapper.insert(student);
        return insert1>0 && insert2>0;
    }

    public Boolean deleteStudentByUserId(String userId){
        boolean delete1 = userService.deleteByUserId(userId);
        boolean delete2 = studentMapper.deleteByUserId(userId);
        return delete2&&delete1;
    }

    public Boolean updateStudentByUserId(StudentDto dto){
        User user = new User();
        Student student = new Student();
        user.setUserId(dto.getUserId());
        user.setUid(dto.getUid());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setBesides(dto.getBesides());
        user.setIsActive(dto.getIsActive());

        student.setGroups(dto.getGroups());
        student.setUserId(dto.getUserId());
        student.setUuid(dto.getUuid());
        student.setGrade(dto.getGrade());
        student.setSchool(dto.getSchool());
        student.setMajor(dto.getMajor());
        student.setCollege(dto.getCollege());
        student.setBesides(dto.getBesides());

        boolean update1 = userMapper.updateByUserId(user);
        boolean update2 = studentMapper.updateByUserId(student);
        return update1&&update2;
    }

    public Boolean deleteBatchByUserIds(List<String> userIds){
        Boolean delete2=studentMapper.deleteByUserIds(userIds);
        Boolean delete1=userService.deleteByUserIds(userIds);
        return delete2&&delete1;
    }


    @Transactional
    public Boolean insertBatchStudents(List<StudentDto> dtoList){
        for(StudentDto dto:dtoList){
            insertStudent(dto);
        }
        return true;
    }


    //分页查询，搜索查询
    public IPage<StudentVo> page(StudentSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size    = dto.getSize() == null ? 10 : dto.getSize();

        // 1. 根据 student 侧条件只拿 user_id 集合（投影查询，内存忽略不计）
        List<String> stuUserIds = studentMapper.selectObjs(
                Wrappers.<Student>lambdaQuery()
                        .select(Student::getUserId)   // 只查一列
                        .eq(StringUtils.hasText(dto.getUuid()),   Student::getUuid,   dto.getUuid())
                        .like(StringUtils.hasText(dto.getGrade()), Student::getGrade,  dto.getGrade())
                        .like(StringUtils.hasText(dto.getMajor()), Student::getMajor,  dto.getMajor())
                        .like(StringUtils.hasText(dto.getSchool()), Student::getSchool, dto.getSchool())
                        .like(StringUtils.hasText(dto.getGroups()),Student::getGroups,dto.getGroups())
                        .like(StringUtils.hasText(dto.getCollege()), Student::getCollege, dto.getCollege())
        ).stream().map(String::valueOf).toList();

        if (stuUserIds.isEmpty()) {
            return new Page<>(page, size);   // 没交集，直接空页
        }

        // 2. 根据 user 侧条件只拿 user_id 集合
        List<String> usrUserIds = userMapper.selectObjs(
                Wrappers.<User>lambdaQuery()
                        .select(User::getUserId)
                        .like(StringUtils.hasText(dto.getUid()),   User::getUid,   dto.getUid())
                        .like(StringUtils.hasText(dto.getName()),  User::getName,  dto.getName())
                        .like(StringUtils.hasText(dto.getPhone()), User::getPhone, dto.getPhone())
                        .like(StringUtils.hasText(dto.getRole()),  User::getRole,  dto.getRole())
                        .eq(dto.getIsActive() != null, User::getIsActive, dto.getIsActive())
        ).stream().map(String::valueOf).toList();

        if (usrUserIds.isEmpty()) {
            return new Page<>(page, size);
        }

        // 3. 取交集（小集合，内存可忽略）
        Set<String> intersect = new HashSet<>(stuUserIds);
        intersect.retainAll(usrUserIds);
        if (intersect.isEmpty()) {
            return new Page<>(page, size);
        }

        // 4. 用交集做真分页——这里选择对 Student 分页（也可对 User，逻辑一样）
        Page<Student> stuPage = new Page<>(page, size);
        Page<Student> pageStu = studentMapper.selectPage(stuPage,
                Wrappers.<Student>lambdaQuery()
                        .in(Student::getUserId, intersect)
                // 如果 student 侧还有排序，在这里加 .orderBy...
        );

        // 5. 拿当前页 user_id 一次性补 User
        List<String> pageUids = pageStu.getRecords()
                .stream()
                .map(Student::getUserId)
                .toList();
        List<User> pageUsers = userMapper.selectList(
                Wrappers.<User>lambdaQuery().in(User::getUserId, pageUids));
        Map<String, User> userMap = pageUsers.stream()
                .collect(Collectors.toMap(User::getUserId, u -> u));

        // 6. 组装 VO
        List<StudentVo> records = new ArrayList<>(pageStu.getRecords().size());
        for (Student stu : pageStu.getRecords()) {
            User u = userMap.get(stu.getUserId());   // 保证非空
            records.add(toStudentVo(u, stu));
        }

        // 7. 回填分页对象
        return new Page<StudentVo>(page, size)
                .setRecords(records)
                .setTotal(intersect.size());   // 总条数就是交集大小
    }


    private StudentVo toStudentVo(User u, Student s) {
        StudentVo vo = new StudentVo();
        vo.setUserId(u.getUserId());
        vo.setUid(u.getUid());
        vo.setName(u.getName());
        vo.setPhone(u.getPhone());
        vo.setRole(u.getRole());
        vo.setIsActive(u.getIsActive());
        vo.setImageUrl(u.getImageUrl());
        if (s != null) {
            vo.setUuid(s.getUuid());
            vo.setGrade(s.getGrade());
            vo.setMajor(s.getMajor());
            vo.setSchool(s.getSchool());
            vo.setCollege(s.getCollege());
            vo.setGroups(s.getGroups());
        }
        return vo;

    }




}
