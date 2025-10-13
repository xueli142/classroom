package org.example.demo5.service.User;

import org.example.demo5.mapper.User.RoleMapper;
import org.example.demo5.mapper.User.TeacherMapper;
import org.example.demo5.model.User.Teacher;
import org.example.demo5.util.JwtUtil;
import org.example.demo5.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService extends BaseService<Teacher> {

    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherMapper teacherMapper,
                          RoleMapper roleMapper,
                          PasswordUtil passwordUtil,
                          JwtUtil jwtUtil) {
        super(teacherMapper, roleMapper, passwordUtil, jwtUtil, "TEACHER");
        this.teacherMapper = teacherMapper;
    }

    /* -------------- Teacher 特有 -------------- */
    public boolean addOne(Teacher t) { return teacherMapper.insertCheck(t); }
    public boolean updOne(Teacher t) { return teacherMapper.updateByIdCheck(t); }
    public Teacher getOne(Integer id) { return teacherMapper.selectById(id); }

    @Transactional
    public int addBatch(List<Teacher> list) { return teacherMapper.insertBatch(list); }

    @Transactional
    public int delBatch(List<Integer> ids) { return teacherMapper.deleteBatchIds(ids); }

    @Transactional
    public int updBatch(List<Teacher> list) { return teacherMapper.updateBatchById(list); }

    public List<Teacher> listByIds(List<Integer> ids) { return teacherMapper.selectBatchByIds(ids); }
    public List<Teacher> listByUids(List<String> uids) { return teacherMapper.selectBatchByUids(uids); }
}