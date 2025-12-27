package org.example.classroom.dao.classroom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import org.example.classroom.model.classroom.CourseSlot;
import org.example.classroom.model.classroom.StudentCourse;

import java.util.List;
import java.util.stream.Collectors;

public interface CourseSlotMapper extends BaseMapper<CourseSlot> {
    default Boolean deleteBySlotId(String slotId) {
        return delete(Wrappers.<CourseSlot>lambdaQuery()
                .eq(CourseSlot::getCourseSlotId, slotId)) > 0;
    }
    default  CourseSlot selectOneBySlotId(String slotId){


        return selectOne(Wrappers.<CourseSlot>lambdaQuery()
                .eq(CourseSlot::getCourseId , slotId));
    }

    default String selectCourseIdBySlotId(String slotId){
       CourseSlot slot= selectOne(Wrappers.<CourseSlot>lambdaQuery()
               .eq(CourseSlot::getCourseSlotId,slotId));
       return slot.getCourseId();
    }
    /**
     * 批量根据 slotId 查 courseId，顺序与参数一致
     */
    default List<String> selectCourseIdsBySlotIds(@Param("slotIds") List<String> slotIds) {
        return selectList(Wrappers.<CourseSlot>lambdaQuery()
                .in(CourseSlot::getCourseSlotId, slotIds))
                .stream()
                .map(CourseSlot::getCourseId)
                .collect(Collectors.toList());
    }
    default Boolean deleteBySlotIds(List<String> slotIds) {
        return delete(Wrappers.<CourseSlot>lambdaQuery()
                .in(CourseSlot::getCourseSlotId, slotIds)) > 0;
    }

    default Boolean updateBySlotId(CourseSlot slot) {
        return update(slot, Wrappers.<CourseSlot>lambdaQuery()
                .eq(CourseSlot::getCourseSlotId, slot.getCourseSlotId())) > 0;
    }


    public class CourseSlotBatchProvider {

        public String updateBatchSomeColumn(@Param("list") List<CourseSlot> list) {
            StringBuilder sql = new StringBuilder(512);

            /* 1. UPDATE 头部 */
            sql.append("UPDATE course_slot SET ");

            /* 2. 每一列拼一段 CASE WHEN */
            /*    为了代码短，只示范 4 个字段，需要更多按同样格式加 */
            StringBuilder caseCourseId      = new StringBuilder(128);
            StringBuilder caseTeacherId     = new StringBuilder(128);
            StringBuilder caseClassroomId   = new StringBuilder(128);
            StringBuilder caseStatus        = new StringBuilder(128);
            StringBuilder idList            = new StringBuilder(64);

            for (int i = 0; i < list.size(); i++) {
                CourseSlot s = list.get(i);
                String id = s.getCourseSlotId();

                /* 收集 IN 里的 id */
                if (i > 0) idList.append(',');
                idList.append('\'').append(id).append('\'');

                /* 拼各字段的 CASE 分支 */
                if (s.getCourseId() != null) {
                    caseCourseId.append(" WHEN '").append(id)
                            .append("' THEN #{list[").append(i).append("].courseId}");
                }
                if (s.getTeacherId() != null) {
                    caseTeacherId.append(" WHEN '").append(id)
                            .append("' THEN #{list[").append(i).append("].teacherId}");
                }
                if (s.getClassroomId() != null) {
                    caseClassroomId.append(" WHEN '").append(id)
                            .append("' THEN #{list[").append(i).append("].classroomId}");
                }
                if (s.getStatus() != null) {
                    caseStatus.append(" WHEN '").append(id)
                            .append("' THEN #{list[").append(i).append("].status}");
                }
            }

            /* 3. 把 CASE 段落组装进主 SQL */
            /*    只要该字段出现过就拼 */
            if (!caseCourseId.isEmpty()) {
                sql.append("course_id = CASE course_slot_id ").append(caseCourseId).append(" END,");
            }
            if (!caseTeacherId.isEmpty()) {
                sql.append("teacher_id = CASE course_slot_id ").append(caseTeacherId).append(" END,");
            }
            if (!caseClassroomId.isEmpty()) {
                sql.append("classroom_id = CASE course_slot_id ").append(caseClassroomId).append(" END,");
            }
            if (!caseStatus.isEmpty()) {
                sql.append("status = CASE course_slot_id ").append(caseStatus).append(" END,");
            }

            /* 4. 去掉最后一个逗号 */
            sql.setLength(sql.length() - 1);

            /* 5. WHERE 范围 */
            sql.append(" WHERE course_slot_id IN (").append(idList).append(')');

            return sql.toString();
        }
    }
    /**
     * 根据 course_slot_id 批量更新部分字段
     * @param list 实体集合里非空字段都会被更新
     */
    @UpdateProvider(type = CourseSlotBatchProvider.class, method = "updateBatchSomeColumn")
    int updateBatchSomeColumn(@Param("list") List<CourseSlot> list);





}
