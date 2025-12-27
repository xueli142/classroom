package org.example.classroom.transfer.dto.simpleVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.classroom.model.classroom.CourseSlot;

import java.util.List;
@AllArgsConstructor   // ← 生成三个参数的构造器
@NoArgsConstructor    // ← 生成无参构造（JSON 反序列化需要）
@Data
public class CourseTableVo {

    private Integer slot;          // 第几节课 0-11
    private Integer dayOfWeek;
    private List<CourseSlot> courses;

}
