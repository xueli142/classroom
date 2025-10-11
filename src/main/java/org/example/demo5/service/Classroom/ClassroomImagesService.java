package org.example.demo5.service.Classroom;

import jakarta.annotation.PostConstruct;
import org.example.demo5.mapper.Classroom.ClassroomImagesMapper;
import org.example.demo5.model.Classroom.ClassroomImages;
import org.example.demo5.util.IdGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ClassroomImagesService {
    @Autowired
    ClassroomImagesMapper imagesMapper;

    public ClassroomImages getImageByClassroomId(String id) {
        return imagesMapper.selectByClassroomId(id);
    }



}