package org.example.demo5.mapper.User;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.demo5.model.User.BaseUser;

/**
 * 通用 UserMapper，所有子接口继承即可拥有 insertCheck / updateByIdCheck / selectByUid
 */
public interface BaseUserMapper<T extends BaseUser> extends BaseMapper<T> {

    /* --------------- 插入检查 --------------- */
    default boolean insertCheck(T user) {
        boolean clash = exists(new QueryWrapper<T>()
                .eq("uuid", user.getUuid())
                .or(w -> w.eq("email", user.getEmail())
                        .or().eq("phone", user.getPhone())));
        return !clash && insert(user) > 0;
    }

    /* --------------- 更新检查 --------------- */
    default boolean updateByIdCheck(T user) {
        boolean clash = exists(new QueryWrapper<T>()
                .ne("id", user.getId())
                .and(w -> w.eq("uuid", user.getUuid())
                        .or().eq("email", user.getEmail())
                        .or().eq("phone", user.getPhone())));
        return !clash && updateById(user) > 0;
    }

    /* 按 uid 查询 */
    default T selectByUid(String uid) {
        return selectOne(new QueryWrapper<T>().eq("uid", uid));
    }
}