package org.example.demo5.mapper.User;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.example.demo5.model.User.BaseUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 通用 UserMapper，单个 + 批量 增删改查 + 唯一性校验
 */
public interface BaseUserMapper<T extends BaseUser> extends BaseMapper<T> {

    /* ==================== 单条操作 ==================== */
    default boolean insertCheck(T user) {
        boolean clash = exists(new QueryWrapper<T>()
                .eq("uuid", user.getUuid())
                .or(w -> w.eq("email", user.getEmail())
                        .or().eq("phone", user.getPhone())));
        return !clash && insert(user) > 0;
    }

    default boolean updateByIdCheck(T user) {
        boolean clash = exists(new QueryWrapper<T>()
                .ne("id", user.getId())
                .and(w -> w.eq("uuid", user.getUuid())
                        .or().eq("email", user.getEmail())
                        .or().eq("phone", user.getPhone())));
        return !clash && updateById(user) > 0;
    }

    default T selectByUid(String uid) {
        return selectOne(new QueryWrapper<T>().eq("uid", uid));
    }

    /* ==================== 批量插入 ==================== */
    default int insertBatch(Collection<T> list) {
        return insertBatch(list, 1000);
    }

    /**
     * 批量插入（带唯一性校验）
     */
    default int insertBatch(@Param("list") Collection<T> list, int batchSize) {
        if (CollectionUtils.isEmpty(list)) return 0;
        list.forEach(this::insertCheck);   // 复用单条校验
        return insertBatchSomeColumn(list, batchSize);
    }

    int insertBatchSomeColumn(Collection<T> list, int batchSize);

    /* ==================== 批量删除 ==================== */
    default int deleteBatchIds(Collection<?> ids) {
        return deleteBatchIds(ids, 1000);
    }

    int deleteBatchIds(@Param("ids") Collection<?> ids, int batchSize);

    /* ==================== 批量更新 ==================== */
    default int updateBatchById(Collection<T> list) {
        return updateBatchById(list, 1000);
    }

    int updateBatchById(@Param(Constants.COLLECTION) Collection<T> list, int batchSize);

    /* ==================== 批量查询 ==================== */
    default List<T> selectBatchByIds(Collection<? extends Serializable> ids) {
        return selectBatchIds(ids);
    }

    default List<T> selectBatchByUids(Collection<String> uids) {
        return selectList(new QueryWrapper<T>().in("uid", uids));
    }

}