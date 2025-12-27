package org.example.classroom.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.example.classroom.model.user.Admin;

import java.util.List;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {


    default Boolean updatedAdminByUserId(Admin admin) {
        return this.update(admin, Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUserId, admin.getUserId())
        )>0;
    }
    default Boolean deleteByUserId(String userId){
        return this.delete(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUserId,userId)
        )>0;
    }

    default  Boolean deleteByUserIds(List<String> userIds){
        return this.delete(Wrappers.<Admin>lambdaQuery()
                .in(Admin::getUserId,userIds))>0;
    }
}
