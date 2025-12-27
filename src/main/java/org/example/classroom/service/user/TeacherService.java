package org.example.classroom.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.user.TeacherMapper;
import org.example.classroom.dao.user.UserMapper;
import org.example.classroom.transfer.dto.insertDto.TeacherDto;
import org.example.classroom.transfer.dto.selectDto.TeacherSelectDto;
import org.example.classroom.transfer.dto.simpleVo.TeacherVo;
import org.example.classroom.model.user.Teacher;
import org.example.classroom.model.user.User;
import org.example.classroom.util.PasswordUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    PasswordUtil passwordUtil;
    @Resource
    private UserService userService;


    @Transactional
    public Boolean insertTeacher(TeacherDto dto){
        User user = new User();
        Teacher teacher = new Teacher();
        String userId = UUID.randomUUID().toString();

        user.setUid(dto.getUid());
        user.setName(dto.getName());
        user.setPassword(passwordUtil.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRole("TEACHER");
        user.setUserId(userId);
        user.setBesides(dto.getBesides());
        user.setIsActive(dto.getIsActive());

        teacher.setUserId(userId);
        teacher.setUuid(dto.getUuid());
        teacher.setBesides(dto.getBesides());
        teacher.setCollege(dto.getCollege());
        teacher.setSchool(dto.getSchool());

        int insert1 = userMapper.insert(user);
        int insert2 = teacherMapper.insert(teacher);
        return insert1>0 && insert2>0;
    }


    public Boolean deleteTeacherByUserId(String userId){
        boolean delete1 = userService.deleteByUserId(userId);
        boolean delete2 = teacherMapper.deleteByUserId(userId);
        return delete2&&delete1;
    }

    public Boolean deleteTeacherByUserIds(List<String> ids){
        boolean delete1 = userService.deleteByUserIds(ids);
        boolean delete2 = teacherMapper.deleteByUserIds(ids);
        return delete2&&delete1;
    }

    public Boolean updateTeacherByUserId(TeacherDto dto){
        User user = new User();
        Teacher teacher = new Teacher();

        user.setUid(dto.getUid());
        user.setName(dto.getName());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordUtil.encode(dto.getPassword()));
        }
        user.setPhone(dto.getPhone());
        user.setBesides(dto.getBesides());
        user.setIsActive(dto.getIsActive());

        teacher.setUserId(dto.getUserId());
        teacher.setUuid(dto.getUuid());
        teacher.setBesides(dto.getBesides());
        teacher.setCollege(dto.getCollege());
        teacher.setSchool(dto.getSchool());

        boolean update1 = userMapper.updateByUserId(user);
        boolean update2 = teacherMapper.updateByUserId(teacher);
        return  update1 && update2;
    }


    public IPage<TeacherVo> page(TeacherSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();

        /* 1. 根据 teacher 侧条件只拿 user_id 集合 */
        List<String> teaUserIds = teacherMapper.selectObjs(
                Wrappers.<Teacher>lambdaQuery()
                        .select(Teacher::getUserId)
                        .like(StringUtils.hasText(dto.getUuid()),    Teacher::getUuid,    dto.getUuid())
                        .like(StringUtils.hasText(dto.getCollege()), Teacher::getCollege, dto.getCollege())
                        .like(StringUtils.hasText(dto.getSchool()),  Teacher::getSchool,  dto.getSchool())
        ).stream().map(String::valueOf).toList();

        if (teaUserIds.isEmpty()) {
            return new Page<>(page, size);
        }

        /* 2. 根据 user 侧条件只拿 user_id 集合 */
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

        /* 3. 取交集 */
        Set<String> intersect = new HashSet<>(teaUserIds);
        intersect.retainAll(usrUserIds);
        if (intersect.isEmpty()) {
            return new Page<>(page, size);
        }

        /* 4. 用交集对 Teacher 做真分页 */
        Page<Teacher> teaPage = new Page<>(page, size);
        Page<Teacher> pageTea = teacherMapper.selectPage(teaPage,
                Wrappers.<Teacher>lambdaQuery()
                        .in(Teacher::getUserId, intersect)
                /* 如需排序：.orderByAsc/Desc(...) */
        );

        /* 5. 一次性补 User 信息 */
        List<String> pageUids = pageTea.getRecords()
                .stream()
                .map(Teacher::getUserId)
                .toList();
        List<User> pageUsers = userMapper.selectList(
                Wrappers.<User>lambdaQuery().in(User::getUserId, pageUids));
        Map<String, User> userMap = pageUsers.stream()
                .collect(Collectors.toMap(User::getUserId, u -> u));

        /* 6. 组装 VO */
        List<TeacherVo> records = pageTea.getRecords()
                .stream()
                .map(t -> toTeacherVo(userMap.get(t.getUserId()), t))
                .toList();

        /* 7. 返回分页对象 */
        return new Page<TeacherVo>(page, size)
                .setRecords(records)
                .setTotal(intersect.size());
    }

    /* 7. 简单转换 */
    private TeacherVo toTeacherVo(User u, Teacher t) {
        TeacherVo vo = new TeacherVo();
        vo.setUserId(u.getUserId());
        vo.setUid(u.getUid());
        vo.setName(u.getName());
        vo.setPhone(u.getPhone());
        vo.setRole(u.getRole());
        vo.setIsActive(u.getIsActive());
        vo.setImageUrl(u.getImageUrl());
        if (t != null) {
            vo.setUuid(t.getUuid());
            vo.setCollege(t.getCollege());
            vo.setSchool(t.getSchool());
        }
        return vo;
    }




















































}
