package org.example.classroom.service.classroom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.ClassroomMapper;
import org.example.classroom.transfer.dto.insertDto.ClassroomDto;
import org.example.classroom.transfer.dto.selectDto.ClassroomSelectDto;
import org.example.classroom.model.classroom.Classroom;
import org.example.classroom.util.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class ClassroomService {

    @Resource
    ClassroomMapper classroomMapper;
    @Resource
    ImageUtil imageUtil;

    public Boolean insertClassroom (ClassroomDto dto, MultipartFile file){

        Classroom classroom=new Classroom();
        String url = imageUtil.saveImage(file);
        String classroomId = UUID.randomUUID().toString();

        classroom.setType(dto.getType());
        classroom.setImageUrl(url);
        classroom.setClassroomId(classroomId);
        classroom.setName(dto.getName());
        classroom.setBesides(dto.getBesides());
        classroom.setDescription(dto.getDescription());
        classroom.setIsActive(dto.getIsActive());
        classroom.setNumber(dto.getNumber());
        classroom.setLocation(dto.getLocation());
        return classroomMapper.insert(classroom)>0;

    }

    public Boolean updateClassroom(ClassroomDto dto ,MultipartFile file ,String oldName){

        Classroom classroom=new Classroom();
        if(file!=null){
            String url=imageUtil.changeImage(file,oldName);
            classroom.setImageUrl(url);
        }





        classroom.setType(dto.getType());
        classroom.setClassroomId(dto.getClassroomId());
        classroom.setName(dto.getName());
        classroom.setBesides(dto.getBesides());
        classroom.setDescription(dto.getDescription());
        classroom.setIsActive(dto.getIsActive());
        classroom.setNumber(dto.getNumber());
        classroom.setLocation(dto.getLocation());


        return classroomMapper.updateByClassroomId(classroom);


    }

    public Boolean deleteByClassroomId(String classroomId) {
        Boolean result = classroomMapper.deleteByClassroomId(classroomId);
        String url = classroomMapper.selectUrlByClassroomId(classroomId);

        if (url == null) {
            return false;
        }
        return imageUtil.deleteImage(url) && result;
    }

    public Boolean deleteByClassroomIds(List<String> classroomIds) {
        Boolean result1 = classroomMapper.deleteByClassroomIds(classroomIds);
        List<String> urls = classroomMapper.selectUrlByClassroomIds(classroomIds);
        Boolean result2 = imageUtil.deleteImages(urls);
        return result2 && result1;
    }

    public IPage<Classroom> classroomIPage(ClassroomSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();

        Page<Classroom> pg = new Page<>(page, size);
        LambdaQueryWrapper<Classroom> wrapper = new LambdaQueryWrapper<>();

        // 教室名称模糊
        wrapper.like(StringUtils.hasText(dto.getName()), Classroom::getName, dto.getName());
        // 位置模糊
        wrapper.like(StringUtils.hasText(dto.getLocation()), Classroom::getLocation, dto.getLocation());
        // 类型精确
        wrapper.eq(StringUtils.hasText(dto.getType()), Classroom::getType, dto.getType());
        // 容纳人数精确
        wrapper.eq(dto.getNumber() != null, Classroom::getNumber, dto.getNumber());
        // 状态精确
        wrapper.eq(dto.getIsActive() != null, Classroom::getIsActive, dto.getIsActive());
        // 描述模糊
        wrapper.like(StringUtils.hasText(dto.getDescription()), Classroom::getDescription, dto.getDescription());
        // 备注模糊
        wrapper.like(StringUtils.hasText(dto.getBesides()), Classroom::getBesides, dto.getBesides());
        // 主键精确（如果接口允许按 id 查询）
        wrapper.eq(dto.getClassroomId() != null, Classroom::getClassroomId, dto.getClassroomId());

        return classroomMapper.selectPage(pg, wrapper);   // 直接用 MyBatis-Plus 自带方法即可
    }

}
