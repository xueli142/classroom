package org.example.demo5.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo5.model.User.Student;
import org.example.demo5.model.User.Teacher;
import org.example.demo5.service.User.StudentService;
import org.example.demo5.service.User.TeacherService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final StudentService studentService;
    private final TeacherService teacherService;

    /* ==================== Student ==================== */

    @PostMapping("/students")
    public boolean addStu(@RequestBody Student s) {
        return studentService.addOne(s);
    }

    @PutMapping("/students")
    public boolean updStu(@RequestBody Student s) {
        return studentService.updOne(s);
    }

    @GetMapping("/students/{id}")
    public Student getStu(@PathVariable Integer id) {
        return studentService.getOne(id);
    }

    @PostMapping("/students/batch")
    @Transactional
    public int addStuBatch(@RequestBody List<Student> list) {
        return studentService.addBatch(list);
    }

    @DeleteMapping("/students/batch")
    @Transactional
    public int delStuBatch(@RequestBody List<Integer> ids) {
        return studentService.delBatch(ids);
    }

    @PutMapping("/students/batch")
    @Transactional
    public int updStuBatch(@RequestBody List<Student> list) {
        return studentService.updBatch(list);
    }

    @GetMapping("/students/batch/ids")
    public List<Student> listStuByIds(@RequestParam List<Integer> ids) {
        return studentService.listByIds(ids);
    }

    @GetMapping("/students/batch/uids")
    public List<Student> listStuByUids(@RequestParam List<String> uids) {
        return studentService.listByUids(uids);
    }

    /* ==================== Teacher ==================== */

    @PostMapping("/teachers")
    public boolean addTea(@RequestBody Teacher t) {
        return teacherService.addOne(t);
    }

    @PutMapping("/teachers")
    public boolean updTea(@RequestBody Teacher t) {
        return teacherService.updOne(t);
    }

    @GetMapping("/teachers/{id}")
    public Teacher getTea(@PathVariable Integer id) {
        return teacherService.getOne(id);
    }

    @PostMapping("/teachers/batch")
    @Transactional
    public int addTeaBatch(@RequestBody List<Teacher> list) {
        return teacherService.addBatch(list);
    }

    @DeleteMapping("/teachers/batch")
    @Transactional
    public int delTeaBatch(@RequestBody List<Integer> ids) {
        return teacherService.delBatch(ids);
    }

    @PutMapping("/teachers/batch")
    @Transactional
    public int updTeaBatch(@RequestBody List<Teacher> list) {
        return teacherService.updBatch(list);
    }

    @GetMapping("/teachers/batch/ids")
    public List<Teacher> listTeaByIds(@RequestParam List<Integer> ids) {
        return teacherService.listByIds(ids);
    }

    @GetMapping("/teachers/batch/uids")
    public List<Teacher> listTeaByUids(@RequestParam List<String> uids) {
        return teacherService.listByUids(uids);
    }
}