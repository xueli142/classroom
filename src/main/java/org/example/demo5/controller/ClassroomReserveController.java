package org.example.demo5.controller;

import org.example.demo5.dto.*;
import org.example.demo5.model.Classroom.Reservations;
import org.example.demo5.service.Classroom.ClassroomReserveService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/classroom-reserve")
public class ClassroomReserveController {

    @Resource
    private ClassroomReserveService classroomReserveService;

    /* ===================== 1. 批量新增（单条/多条） ===================== */
    @PostMapping("/batch")
    public ResponseDto<Integer> batchInsert(@RequestBody List<ClassroomReserveDto> dtoList) {
        int rows = classroomReserveService.batchInsertReservations(dtoList);
        return ResponseDto.success(rows, "新增成功 " + rows + " 条");
    }

    /* ===================== 2. 批量全量更新（单条/多条） ===================== */
    @PutMapping("/batch")
    public ResponseDto<Integer> batchUpdate(@RequestBody List<ClassroomReserveDto> dtoList) {
        int rows = classroomReserveService.batchUpdateReservations(dtoList);
        return ResponseDto.success(rows, "更新成功 " + rows + " 条");
    }

    /* ===================== 3. 批量选择性更新（只更新非 null 字段） ===================== */
    @PatchMapping("/batch")
    public ResponseDto<Integer> batchSelectiveUpdate(@RequestBody List<ClassroomReserveDto> dtoList) {
        int rows = classroomReserveService.batchSelectiveUpdate(dtoList);
        return ResponseDto.success(rows, "选择性更新成功 " + rows + " 条");
    }

    /* ===================== 4. 批量删除（单条/多条） ===================== */
    @DeleteMapping("/batch")
    public ResponseDto<Integer> batchDelete(@RequestBody List<Long> idList) {
        int rows = classroomReserveService.batchDeleteReservations(idList);
        return ResponseDto.success(rows, "删除成功 " + rows + " 条");
    }

    /* ===================== 5. 单条查询（按主键） ===================== */
    @GetMapping("/{id}")
    public ResponseDto<Reservations> getOne(@PathVariable Long id) {
        Reservations entity = classroomReserveService.getById(id);
        return entity == null
                ? ResponseDto.error(404, "记录不存在")
                : ResponseDto.success(entity);
    }

    /* ===================== 6. 批量查询（按主键列表） ===================== */
    @PostMapping("/list-by-ids")
    public ResponseDto<List<Reservations>> listByIds(@RequestBody List<Long> idList) {
        List<Reservations> list = classroomReserveService.listByIds(idList);
        return ResponseDto.success(list);
    }
}