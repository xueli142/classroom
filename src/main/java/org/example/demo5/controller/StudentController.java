package org.example.demo5.controller;


import lombok.RequiredArgsConstructor;
import org.example.demo5.dto.ResponseDto;
import org.example.demo5.model.Classroom.Classroom;
import org.example.demo5.service.Classroom.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {
    @Autowired
    ClassroomService classroomService;
    @GetMapping("/classroom")
    public ResponseDto getClassrooms(@RequestBody String id) {
        ResponseDto classroom = classroomService.selectClassroom(id);
        return classroom;
    }

    @GetMapping("/allclassrooms")
    public ResponseDto getClassroomImages( @RequestParam(defaultValue = "0")int id) {
        List<Classroom> classrooms = classroomService.pageByIdAsc(id);
        List<String> images =  classroomService.pageByUrl(classrooms);
        Map<String,Object>data = Map.of("classrooms",classrooms,"images",images);

        return ResponseDto.success(data);
    }

}
