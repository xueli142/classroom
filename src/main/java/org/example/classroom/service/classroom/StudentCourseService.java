package org.example.classroom.service.classroom;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.classroom.dao.classroom.StudentCourseMapper;
import org.example.classroom.transfer.dto.selectDto.StudentCourseSelectDto;
import org.example.classroom.model.classroom.StudentCourse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class StudentCourseService {
    @Resource
    StudentCourseMapper studentCourseMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;


    public Boolean insertStudentCourse(StudentCourse studentCourse){

            studentCourse.setStudentCourseId(UUID.randomUUID().toString());
        return studentCourseMapper.insert(studentCourse)>0;
    }


    public Boolean insertStudentCourseBatch(List<StudentCourse> list){


        list.forEach(item->
                item.setStudentCourseId(UUID.randomUUID().toString()));
        return insertBatchBySession(list)>0;
    }

    public int insertBatchBySession(List<StudentCourse> list) {
        if (CollectionUtils.isEmpty(list)) return 0;
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            StudentCourseMapper mapper = session.getMapper(StudentCourseMapper.class);
            int batch = 500, i = 0;
            for (StudentCourse sc : list) {
                mapper.insert(sc);          // 单条 insert，底层 addBatch
                if (++i % batch == 0) {
                    session.flushStatements();
                }
            }
            session.commit();
            return list.size();
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public Boolean updateStudentCourse(StudentCourse studentCourse){


        return studentCourseMapper.updateByStudentCourseId(studentCourse);
    }


   public Boolean deleteByUserId(String userId){
        return studentCourseMapper.deleteByUserId(userId);
    }
    public Boolean deleteByCourseId(String courseId) {
        return studentCourseMapper.deleteByCourseId(courseId);
    }

    public Boolean deleteByStudentCourseId(String id) {
        return studentCourseMapper.deleteByStudentCourseId(id);
    }

    /* -------------------- 批量删除 -------------------- */
    public Boolean deleteBatchByUserIds(List<String> userIds) {
        if (CollUtil.isEmpty(userIds)) return true;
        return studentCourseMapper.deleteBatchByUserIds(userIds);
    }

    public Boolean deleteBatchByCourseIds(List<String> courseIds) {
        if (CollUtil.isEmpty(courseIds)) return true;
        return studentCourseMapper.deleteBatchByStudentCourseIds(courseIds);
    }

    public IPage<StudentCourse>studentCoursePage(StudentCourseSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();
        Page<StudentCourse> pg = new Page<>(page, size);
        LambdaQueryWrapper<StudentCourse> wrapper = new LambdaQueryWrapper<StudentCourse>()
                .eq(StringUtils.hasText(dto.getCourseId()), StudentCourse::getCourseId, dto.getCourseId())
                .eq(StringUtils.hasText(dto.getUserId()), StudentCourse::getUserId, dto.getUserId())
                .eq(dto.getStatus() != null, StudentCourse::getStatus, dto.getStatus())
                .eq(StringUtils.hasText(dto.getStudentCourseId()), StudentCourse::getStudentCourseId, dto.getStudentCourseId());

return studentCourseMapper.selectPage(pg,wrapper);
    }

public Boolean insertOne(String userId , String courseId){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId(courseId);
    studentCourse.setStudentCourseId(UUID.randomUUID().toString());
    studentCourse.setUserId(userId);
    studentCourse.setStatus(false);
        return studentCourseMapper.insert(studentCourse)>0;
}


}
