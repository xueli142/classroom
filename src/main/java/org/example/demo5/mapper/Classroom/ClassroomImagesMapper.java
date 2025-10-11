package org.example.demo5.mapper.Classroom;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.demo5.model.Classroom.ClassroomImages;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ClassroomImagesMapper extends BaseMapper<ClassroomImages> {


    /** 根据业务键找教室记录 */
    default ClassroomImages selectByClassroomId(String classroomId) {
        return selectOne(new QueryWrapper<ClassroomImages>()
                .eq("classroom_id", classroomId));

    }
    default List<String> selectUrlsByClassroomIds(@Param("classroomIds") List<String> classroomIds) {
        return selectObjs(
                new QueryWrapper<ClassroomImages>()
                        .select("image_url")
                        .in("classroom_id", classroomIds)
        ).stream()
                .map(String.class::cast)   // 把 Object 转成 String
                .collect(Collectors.toList());
    }
    //这里是先检查是否存在再删除
    default boolean deleteByClassroomId(String classroomId) {
        return delete(new QueryWrapper<ClassroomImages>()
                .eq("classroom_id", classroomId)
                // 加上 exists 子查询，只删存在的记录
                .exists("select 1 from classroom_images ci " +
                        "where ci.classroom_id = #{classroomId}")) > 0;
    }
    //这个是前端校验，如果存在图片，就直接更新，如果不存在，就创建
    default boolean updateByClassroomImages(ClassroomImages classroomImages) {
        return update(classroomImages, new QueryWrapper<ClassroomImages>()
                .eq("classroom_id", classroomImages.getClassroomId())) > 0;
    }


}









