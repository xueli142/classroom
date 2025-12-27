package org.example.classroom.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.classroom.model.user.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper extends BaseMapper<User> {



    default User selectUserByUid(String uid){

        return this.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUid,uid)

        );
    }
    default User selectUserByUserId(String userId){
        return this.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserId,userId));
    }

    default Boolean deleteByUserId(String userId){

        return this.delete(Wrappers.<User>lambdaQuery()
                .eq(User::getUserId, userId)

        )>0;
    }

    default String selectUrlByUserId(String userId){
        return selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserId,userId)
                ).getImageUrl();
    }
    default List<String> selectUrlByUserIds(List<String> userIds) {
        // 使用 selectList 查询 User 对象列表
        List<User> users = selectList(
                Wrappers.<User>lambdaQuery()
                        .in(User::getUserId, userIds)
                        .select(User::getImageUrl) // 只查询 imageUrl 字段
        );

        // 从 User 对象列表中提取 imageUrl 字段，形成字符串列表
        return users.stream()
                .map(User::getImageUrl)
                .filter(Objects::nonNull) // 过滤掉 null 值
                .collect(Collectors.toList());
    }


    default boolean updateByUserId(User user){

        return this.update(user,Wrappers.<User>lambdaQuery()
                .eq(User::getUserId,user.getUserId())

        )>0;
    }
    default boolean deleteByUserIds(List<String> userIds){
        return delete(Wrappers.<User>lambdaQuery()
                .in(User::getUserId,userIds))>0;
    }


}
