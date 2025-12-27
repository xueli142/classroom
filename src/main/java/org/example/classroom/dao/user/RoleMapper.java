package org.example.classroom.dao.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.classroom.model.user.Role;

import java.time.LocalDateTime;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /** 根据业务键找角色 */
    default Role selectByBiz(String userId) {
        return selectOne(new QueryWrapper<Role>()
                .eq("userId", userId));
    }




}
