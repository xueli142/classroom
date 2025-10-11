package org.example.demo5.service.Classroom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.AssertFalse;
import org.example.demo5.dto.ClassroomDto;
import org.example.demo5.dto.ResponseDto;
import org.example.demo5.mapper.Classroom.ClassroomImagesMapper;
import org.example.demo5.mapper.Classroom.ClassroomMapper;
import org.example.demo5.model.Classroom.Classroom;
import org.example.demo5.model.Classroom.ClassroomImages;
import org.example.demo5.util.IdGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassroomService {
//这里构建classroom
    @Autowired
     ClassroomMapper classroomMapper;          // MyBatis-Plus / XML 均可
    @Autowired
     ClassroomImagesMapper imagesMapper;
    public String addClassroom(ClassroomDto dto) {
        Classroom c = new Classroom();
        c.setClassroomId(IdGeneratorUtil.generateUuid());
        c.setName(dto.getName());
        c.setLocation(dto.getLocation());
        c.setNumber(dto.getNumber());
        c.setDescription(dto.getDescription());
        c.setDayOfWeek(dto.getDayOfWeek());
        c.setIsActive(false);


        classroomMapper.insert(c);

        if(dto.getTypeId()!=null){
            //将typeId对应的DayofWeek填充，这里的逻辑先不写
        }

        return c.getClassroomId();
    }
//这里构建classroomImages

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-url-prefix}")
    private String accessUrlPrefix;


    @PostConstruct
    public void init() {
        // 目录不存在就创建
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
    }
    public String addClassroomAndSave(MultipartFile file,ClassroomDto classroomDto) {
        if (file.isEmpty()) throw new IllegalArgumentException("空文件");
        //直接使用上一个函数
        String classroomId = addClassroom(classroomDto);

        String origin = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(origin);
        String uuid = IdGeneratorUtil.generateUuid();   // hutool 工具
        String newName = uuid + "." + ext;


        Path target = Paths.get(uploadDir, newName);
        ClassroomImages classroomImages=new ClassroomImages();

        classroomImages.setClassroomId(classroomId);
        classroomImages.setImageUrl(target.toString());
        

        try {
            Files.copy(file.getInputStream(), target);
        } catch (IOException e) {
            throw new RuntimeException("保存失败", e);
        }
        // 返回对外 URL
        return accessUrlPrefix + newName;
    }
    public String saveImage(String classroomId, MultipartFile file) {
        // 直接用你原来的“保存文件+返回URL”逻辑即可
        if (file.isEmpty()) throw new IllegalArgumentException("空文件");
        String origin = file.getOriginalFilename();
        String ext  = StringUtils.getFilenameExtension(origin);
        String newName = IdGeneratorUtil.generateUuid() + "." + ext;

        Path target = Paths.get(uploadDir, newName);
        try {
            Files.copy(file.getInputStream(), target);
        } catch (IOException e) {
            throw new RuntimeException("保存失败", e);
        }
        // 把图片路径写到 classroom_images 表（略）
        ClassroomImages ci = new ClassroomImages();
        ci.setClassroomId(classroomId);
        ci.setImageUrl(target.toString());
        imagesMapper.insert(ci);

        return accessUrlPrefix + newName;
    }

    public ResponseDto selectClassroom(String id){
        ClassroomImages images = imagesMapper.selectByClassroomId(id);
        if (images == null) {
            return  ResponseDto.error( "没有找到对应的教室图片");
        }
        Classroom classroom = classroomMapper.selectById(id);
        if(classroom == null){
            return ResponseDto.error("没有找到对应的教室");
        }


        return ResponseDto.success( classroom, images.getImageUrl());
    }


    //这里前端传递进去page，后端根据page查询id
    public List<Classroom> pageByIdAsc(int page) {
        Page<Classroom> p = Page.of(page + 1, 10);   // MP 的页码从 1 开始
        LambdaQueryWrapper<Classroom> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(Classroom::getId);             // 按 id 升序
        return classroomMapper.selectPage(p, qw).getRecords();
    }
    public List<String> pageByUrl(List <Classroom> classrooms){


        List<String> ids = classrooms
                .stream()
                .map(Classroom::getClassroomId)
                .toList();
         // ids 可能是空的
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        return imagesMapper.selectUrlsByClassroomIds(ids);

    }


}
