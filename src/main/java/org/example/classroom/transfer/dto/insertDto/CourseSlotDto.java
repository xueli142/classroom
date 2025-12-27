package org.example.classroom.transfer.dto.insertDto;


import lombok.Data;

@Data
public class CourseSlotDto {
    private String CourseSlotId;
    private Long weekMonday;//第几周
    private Long  dayOfWeek;//在一周的星期几
    private Long slot;//第几节课
    private String CourseName;//课程名字
    private String userId;//特征键，表示这节课属于谁
    private String teacherId;//这节课的老师
    private String courseId;//对应的状态课程
    private String classroomId;//对应的教室
    private String location;//地点
    private String teacherName;
    private Long number;//这节课最大人数
    private Long bookingNumber;//已经预约人数
    private Boolean status;//
    private String besides;//备注
    private String imageUrl;



}
