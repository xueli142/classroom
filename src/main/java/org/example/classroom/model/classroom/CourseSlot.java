
package org.example.classroom.model.classroom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseSlot {

  private Long id;
  private String courseSlotId;//课程特征键
  private String courseId;//对应的状态课程
  private String teacherId;//这节课的老师
  private String classroomId;//对应的教室
  private String userId;//特征键，表示这节课属于谁
  private Long weekMonday;//第几周
  private Long  dayOfWeek;//在一周的星期几
  private Long slot;//第几节课
  private String courseName;//课程名字
  private String location;//地点
  private String teacherName;
  private Long number;//这节课最大人数
  private Long bookingNumber;//已经预约人数
  private Boolean status;//
  private String besides;//备注
  private String imageUrl;//放在课表的图片

  @TableField(fill = FieldFill.INSERT) // 插入时填充
  private LocalDateTime createdTime;
  @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充
  private LocalDateTime updatedTime;

}
