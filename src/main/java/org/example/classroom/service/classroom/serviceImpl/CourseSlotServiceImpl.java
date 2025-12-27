package org.example.classroom.service.classroom.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.example.classroom.dao.classroom.BookingMapper;
import org.example.classroom.dao.classroom.CourseSlotMapper;
import org.example.classroom.service.classroom.service.CourseSlotService;
import org.example.classroom.transfer.dto.insertDto.CourseSlotDto;
import org.example.classroom.transfer.dto.selectDto.CourseSlotSelectDto;
import org.example.classroom.model.classroom.CourseSlot;
import org.example.classroom.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程时段业务层
 * 负责课程时段的增删改、批量插入及图片文件管理
 */
@Service
public class CourseSlotServiceImpl
        extends ServiceImpl<CourseSlotMapper,CourseSlot>  implements CourseSlotService {

    /* ==================== 依赖注入 ==================== */
    @Resource
    private CourseSlotMapper courseSlotMapper; // 纯 MyBatis-Plus BaseMapper
    @Resource
    private ImageUtil imageUtil;               // 项目封装的图片工具类
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private BookingServiceImpl bookingServiceImpl;


    /* ===========================================================
       单条删除：根据主键删除记录（不删图片，如需可自己加）
       =========================================================== */
        public Boolean deleteCourseSlotById(String courseSlotId) {
                   String courseId= courseSlotMapper.selectCourseIdBySlotId(courseSlotId);
            bookingMapper.deleteByCourseId(courseId);
            return courseSlotMapper.deleteBySlotId(courseSlotId);

        }

    /* ===========================================================
       批量删除：传入主键列表，一次性删除多条
       =========================================================== */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCourseSlotByIds(List<String> courseSlotIds) {
        if (CollectionUtils.isEmpty(courseSlotIds)) {
            return true;
        }
        // 1. 批量取 courseId
        List<String> courseIds = courseSlotMapper.selectCourseIdsBySlotIds(courseSlotIds);
        // 2. 先删预约
        bookingMapper.deleteByCourseIds(courseIds);   // 自己再写一条 in (..) 的 SQL
        // 3. 再删课节
        return courseSlotMapper.deleteBySlotIds(courseSlotIds);
    }

    /* ===========================================================
       批量插入：一次上传一张公共图片 -> 给所有时段共用
       事务保证：图片保存成功 + 数据库写入成功 原子提交
       -----------------------------------------------------------
       注意：
       1. 当前实现“一张图”复用到所有时段，业务是否允许？
       2. 真正的 SQL 批量插入依赖 Mapper 的 insertBatchSomeColumn 方法，
          需要提前在 MyBatis-Plus 全局注入器里注册 InsertBatchSomeColumn
          否则启动就会报 “未找到 insertBatchSomeColumn” 错
       3. 如数据量超大，建议再按 500~1000 条分批调 insertBatchSomeColumn，
          或者直接在 ServiceImpl 里用 MyBatis-Plus 提供的 executeBatch
       =========================================================== */
       // 显式指定回滚异常，避免图片已落盘但 DB 失败造成垃圾文件
    @Transactional
    public Boolean insertBatchCourseSlots(List<CourseSlotDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) return true;
        // 放在原方法最前面，零副作用
        dtoList.forEach(d -> {
            if (StringUtils.isBlank(d.getCourseId())) {
                d.setCourseId(UUID.randomUUID().toString());
            }
        });
        // 1. 先按 DTO 里有没有 courseSlotId 拆成两组
        Map<Boolean, List<CourseSlotDto>> split = dtoList.stream()
                .collect(Collectors.partitioningBy(
                        dto -> StringUtils.isNotBlank(dto.getCourseSlotId())));

// 2. 取出两份
        List<CourseSlotDto> updateList   = split.get(true);   // 有值 → 待更新
        List<CourseSlotDto> noIdList    = split.get(false);  // 空值 → 待插入


        //这里的insert，把courseSlotId这个字段没有值的数据加上值
        List<CourseSlot> insertList = noIdList.stream()
                .map(dto -> {
                    CourseSlot po = build(dto);
                    po.setCourseSlotId(UUID.randomUUID().toString());
                    return po;
                }).toList();
        boolean slotOk=true;
        boolean bookOk=true;
        boolean updateOk=true;
        if (insertList.isEmpty()) {
            System.out.println("没有新增的课程槽位，跳过 booking 插入");
        } else {
            slotOk = this.saveBatch(insertList, 1000);
            bookOk = bookingServiceImpl.insertAllBooking(insertList);
        }
        if(updateList.isEmpty()){

        }else{
             updateOk = this.updateCourseSlots(updateList);
        }


        // 3. 真正的批量插入（单条 SQL 多 VALUES）
        return slotOk&&bookOk&&updateOk;
    }

    /* ===========================================================
       构造器：DTO -> PO 字段拷贝
       如果字段很多，建议改用 MapStruct 或 BeanUtils 省代码
       =========================================================== */
    public CourseSlot build(CourseSlotDto dto) {
        CourseSlot slot = new CourseSlot();
        slot.setCourseSlotId(dto.getCourseSlotId());
        slot.setUserId(dto.getUserId());
        slot.setWeekMonday(dto.getWeekMonday());
        slot.setDayOfWeek(dto.getDayOfWeek());
        slot.setSlot(dto.getSlot());
        slot.setCourseId(dto.getCourseId());
        slot.setTeacherId(dto.getTeacherId());
        slot.setClassroomId(dto.getClassroomId());
        slot.setNumber(dto.getNumber());
        slot.setBookingNumber(dto.getBookingNumber());
        slot.setStatus(dto.getStatus());
        slot.setCourseName(dto.getCourseName());
        slot.setLocation(dto.getLocation());
        slot.setTeacherName(dto.getTeacherName());
        slot.setBesides(dto.getBesides());
        slot.setImageUrl(dto.getImageUrl());
        return slot;
    }

    public Boolean updateCourseSlot(CourseSlotDto dto){
        CourseSlot slot = build(dto);
        return courseSlotMapper.updateBySlotId(slot);

    }

    public Boolean updateCourseSlots(List<CourseSlotDto> dtoList){
        List<CourseSlot> slots = dtoList.stream()
                .map(dto -> {
                    CourseSlot slot = build(dto);
                    slot.setCourseSlotId(UUID.randomUUID().toString());

                    return slot;
                })
                .collect(Collectors.toList());
        Boolean result = bookingServiceImpl.insertAllBooking(slots);
        //修改暂时有问题，不可用
        return  (courseSlotMapper.updateBatchSomeColumn(slots)>0)&&result;
    }







    /* ===========================================================
       通用分页查询：根据前端传参动态拼条件
       =========================================================== */
    public IPage<CourseSlot> slotPage(CourseSlotSelectDto dto) {
        // 1. 分页参数
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();
        Page<CourseSlot> pg = new Page<>(page, size);

        // 2. 条件构造
        LambdaQueryWrapper<CourseSlot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dto.getCourseSlotId() != null, CourseSlot::getCourseSlotId, dto.getCourseSlotId())
                .eq(dto.getCourseId() != null, CourseSlot::getCourseId, dto.getCourseId())
                .eq(dto.getTeacherId() != null, CourseSlot::getTeacherId, dto.getTeacherId())
                .eq(dto.getClassroomId() != null, CourseSlot::getClassroomId, dto.getClassroomId())
                .eq(dto.getUserId() != null, CourseSlot::getUserId, dto.getUserId())
                .eq(dto.getWeekMonday() != null, CourseSlot::getWeekMonday, dto.getWeekMonday())
                .eq(dto.getDayOfWeek() != null, CourseSlot::getDayOfWeek, dto.getDayOfWeek())
                .eq(dto.getSlot() != null, CourseSlot::getSlot, dto.getSlot())
                .like(dto.getName() != null, CourseSlot::getCourseName, dto.getName())
                .like(dto.getLocation() != null, CourseSlot::getLocation, dto.getLocation())
                .like(dto.getTeacherName() != null, CourseSlot::getTeacherName, dto.getTeacherName())
                .eq(dto.getNumber() != null, CourseSlot::getNumber, dto.getNumber())
                .eq(dto.getBookingNumber() != null, CourseSlot::getBookingNumber, dto.getBookingNumber())
                .eq(dto.getStatus() != null, CourseSlot::getStatus, dto.getStatus())
                .like(dto.getBesides() != null, CourseSlot::getBesides, dto.getBesides());

        // 3. 倒序排最新
        wrapper.orderByDesc(CourseSlot::getUpdatedTime);

        // 4. 执行分页
        return courseSlotMapper.selectPage(pg, wrapper);
    }

    /**
     * 通用 List 查询：仅按 DTO 中非空字段过滤，无分页
     */
    public List<CourseSlot> listByDto(CourseSlotSelectDto dto) {
        return new LambdaQueryChainWrapper<>(courseSlotMapper)
                .eq(dto.getCourseSlotId() != null, CourseSlot::getCourseSlotId, dto.getCourseSlotId())
                .eq(dto.getCourseId() != null, CourseSlot::getCourseId, dto.getCourseId())
                .eq(dto.getTeacherId() != null, CourseSlot::getTeacherId, dto.getTeacherId())
                .eq(dto.getClassroomId() != null, CourseSlot::getClassroomId, dto.getClassroomId())
                .eq(dto.getUserId() != null, CourseSlot::getUserId, dto.getUserId())
                .eq(dto.getWeekMonday() != null, CourseSlot::getWeekMonday, dto.getWeekMonday())
                .eq(dto.getDayOfWeek() != null, CourseSlot::getDayOfWeek, dto.getDayOfWeek())
                .eq(dto.getSlot() != null, CourseSlot::getSlot, dto.getSlot())
                .like(dto.getName() != null, CourseSlot::getCourseName, dto.getName())
                .like(dto.getLocation() != null, CourseSlot::getLocation, dto.getLocation())
                .like(dto.getTeacherName() != null, CourseSlot::getTeacherName, dto.getTeacherName())
                .eq(dto.getNumber() != null, CourseSlot::getNumber, dto.getNumber())
                .eq(dto.getBookingNumber() != null, CourseSlot::getBookingNumber, dto.getBookingNumber())
                .eq(dto.getStatus() != null, CourseSlot::getStatus, dto.getStatus())
                .like(dto.getBesides() != null, CourseSlot::getBesides, dto.getBesides())
                .orderByDesc(CourseSlot::getUpdatedTime)
                .list();
    }


    /* ===========================================================
       老师端：查看本周自己名下的时段 带分页
       =========================================================== */
    public List<CourseSlot> teacherSlotPage(CourseSlotSelectDto dto) {
        // 只传 teacherId + week_monday 即可
        return listByDto(dto);
    }
    /*
    * 个人端，查看个人的课表
    *
    * */
    public List<CourseSlot> userSlotPage(CourseSlotSelectDto dto){
        return listByDto(dto);
    }


    public List<CourseSlot> classroomOccupyPage(CourseSlotSelectDto dto) {
        return listByDto(dto);
    }

    public List<CourseSlot> studentOccupyPage(CourseSlotSelectDto dto) {
        List<CourseSlot> result1 = listByDto(dto);

        String userId = dto.getUserId();
        if (StringUtils.isBlank(userId)) {
            return Collections.emptyList();          // 安全兜底
        }

        // 1. 查出该学生所有已预约的 slotId
        List<String> bookedSlotIds = bookingMapper.selectSlotIdsByUserId(userId);

        // 2. 空列表保护：没有预约记录时直接给空列表，避免 IN ()
        List<CourseSlot> result2;
        if (bookedSlotIds.isEmpty()) {
            result2 = Collections.emptyList();
        } else {
            result2 = new LambdaQueryChainWrapper<>(courseSlotMapper)
                    .in(CourseSlot::getCourseSlotId, bookedSlotIds)   // 此时 bookedSlotIds 非空
                    .eq(dto.getCourseId()     != null, CourseSlot::getCourseId,     dto.getCourseId())
                    .eq(dto.getTeacherId()    != null, CourseSlot::getTeacherId,    dto.getTeacherId())
                    .eq(dto.getClassroomId()  != null, CourseSlot::getClassroomId,  dto.getClassroomId())
                    .eq(dto.getWeekMonday()   != null, CourseSlot::getWeekMonday,   dto.getWeekMonday())
                    .eq(dto.getDayOfWeek()    != null, CourseSlot::getDayOfWeek,    dto.getDayOfWeek())
                    .eq(dto.getSlot()         != null, CourseSlot::getSlot,         dto.getSlot())
                    .like(dto.getName()       != null, CourseSlot::getCourseName,   dto.getName())
                    .like(dto.getLocation()   != null, CourseSlot::getLocation,     dto.getLocation())
                    .like(dto.getTeacherName()!= null, CourseSlot::getTeacherName,  dto.getTeacherName())
                    .eq(dto.getStatus()       != null, CourseSlot::getStatus,       dto.getStatus())
                    .orderByDesc(CourseSlot::getUpdatedTime)
                    .list();
        }

        // 3. 合并 + 去重（以 result2 为准）
        Map<String, CourseSlot> map = new LinkedHashMap<>();
        result1.forEach(s -> map.putIfAbsent(s.getCourseSlotId(), s));
        result2.forEach(s -> map.put(s.getCourseSlotId(), s));   // 已预约记录覆盖

        return new ArrayList<>(map.values());
    }
}