package org.example.demo5.service.User;

import org.example.demo5.mapper.User.RoleMapper;
import org.example.demo5.mapper.User.StudentMapper;
import org.example.demo5.model.User.Student;
import org.example.demo5.util.JwtUtil;
import org.example.demo5.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service   // 只这一个 @Service，让 Spring 扫进来
public class StudentService extends BaseService<Student> {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper,
                          RoleMapper roleMapper,
                          PasswordUtil passwordUtil,
                          JwtUtil jwtUtil) {
        super(studentMapper, roleMapper, passwordUtil, jwtUtil, "STUDENT");
        this.studentMapper = studentMapper;
    }

    /* -------------- Student 特有 -------------- */
    public boolean addOne(Student s) { return studentMapper.insertCheck(s); }
    public boolean updOne(Student s) { return studentMapper.updateByIdCheck(s); }
    public Student getOne(Integer id) { return studentMapper.selectById(id); }

    @Transactional
    public int addBatch(List<Student> list) { return studentMapper.insertBatch(list); }

    @Transactional
    public int delBatch(List<Integer> ids) { return studentMapper.deleteBatchIds(ids); }

    @Transactional
    public int updBatch(List<Student> list) { return studentMapper.updateBatchById(list); }

    public List<Student> listByIds(List<Integer> ids) { return studentMapper.selectBatchByIds(ids); }
    public List<Student> listByUids(List<String> uids) { return studentMapper.selectBatchByUids(uids); }
}