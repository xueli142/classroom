package org.example.demo5.mapper.User;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.demo5.model.User.Role;

import java.time.LocalDateTime;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /** 根据业务键找角色 */
    default Role selectByBiz(String uid, String uuid) {
        return selectOne(new QueryWrapper<Role>()
                .eq("uid", uid)
                .eq("uuid", uuid));
    }

    /** 业务键变更时同步更新 */
    default int updateUidUuid(String oldUid, String oldUuid,
                              String newUid, String newUuid) {
        return update(null,
                new UpdateWrapper<Role>()
                        .eq("uid", oldUid)
                        .eq("uuid", oldUuid)
                        .set("uid", newUid)
                        .set("uuid", newUuid));
    }


    /** 业务键变更时同步更新 */
    default int updateBizKey(String oldUid, String oldUuid,
                             String newUid, String newUuid) {
        return update(null, new UpdateWrapper<Role>()
                .eq("uid", oldUid).eq("uuid", oldUuid)
                .set("uid", newUid).set("uuid", newUuid));
    }

    /** 首次插入角色记录 */
    default boolean insertBiz(String uid, String uuid, String roleName) {
        Role role = new Role();
        role.setUid(uid);
        role.setUuid(uuid);
        role.setRole(roleName);
        role.setCreatedTime(LocalDateTime.now());
        role.setUpdatedTime(LocalDateTime.now());

        return insert(role) > 0;
    }
}