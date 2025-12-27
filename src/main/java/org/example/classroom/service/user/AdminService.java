package org.example.classroom.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.user.AdminMapper;
import org.example.classroom.dao.user.UserMapper;
import org.example.classroom.transfer.dto.insertDto.AdminDto;
import org.example.classroom.transfer.dto.selectDto.AdminSelectDto;
import org.example.classroom.transfer.dto.simpleVo.AdminVo;
import org.example.classroom.model.user.Admin;
import org.example.classroom.model.user.User;
import org.example.classroom.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    PasswordUtil passwordUtil;
    @Resource
    private UserService userService;

    public Boolean insertAdmin(AdminDto dto) {
        User user = new User();
        Admin admin = new Admin();
        String userId = UUID.randomUUID().toString();

        user.setUserId(userId);
        user.setUid(dto.getUid());
        user.setName(dto.getName());
        user.setPassword(passwordUtil.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRole("ADMIN");
        user.setIsActive(dto.getIsActive());

        admin.setUserId(userId);
        admin.setBesides(dto.getBesides());
        admin.setEmail(dto.getEmail());
        admin.setCreateBy(dto.getCreateBy());
        admin.setRealName(dto.getRealName());

        int insert1 = userMapper.insert(user);
        int insert2 = adminMapper.insert(admin);
        return insert1>10 && insert2>0;
    }

    public Boolean deleteAdminByUserId(String userId){

        Boolean  delete1= userService.deleteByUserId(userId);
        Boolean delete2= adminMapper.deleteByUserId(userId);

        return delete1&&delete2;
    }

    public Boolean deleteAdminByUserIds(List<String> userIds){
        Boolean  delete1= userService.deleteByUserIds(userIds);
        Boolean delete2= adminMapper.deleteByUserIds(userIds);

        return delete1&&delete2;
    }

    public Boolean updateAdminByUserId(AdminDto dto){
        User user = new User();
        Admin admin = new Admin();


        user.setUid(dto.getUid());
        user.setName(dto.getName());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordUtil.encode(dto.getPassword()));
        }
        user.setPhone(dto.getPhone());
        user.setIsActive(dto.getIsActive());


        admin.setUserId(dto.getUserId());
        admin.setBesides(dto.getBesides());
        admin.setEmail(dto.getEmail());
        admin.setCreateBy(dto.getCreateBy());
        admin.setRealName(dto.getRealName());
        admin.setBesides(dto.getBesides());
        Boolean update1 = userMapper.updateByUserId(user);
        Boolean update2 = adminMapper.updatedAdminByUserId(admin);
        return update1 && update2;
    }

    public IPage<AdminVo> pageAdmins(AdminSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();

        /* 1. Admin 侧条件只拿 user_id */
        List<String> admUserIds = adminMapper.selectObjs(
                Wrappers.<Admin>lambdaQuery()
                        .select(Admin::getUserId)
                        .like(StringUtils.hasText(dto.getBesides()),   Admin::getBesides,   dto.getBesides())
                        .like(StringUtils.hasText(dto.getEmail()),     Admin::getEmail,     dto.getEmail())
                        .like(StringUtils.hasText(dto.getRealName()),  Admin::getRealName,  dto.getRealName())
                        .eq(StringUtils.hasText(dto.getCreateBy()),    Admin::getCreateBy,  dto.getCreateBy())
        ).stream().map(String::valueOf).toList();

        if (admUserIds.isEmpty()) {
            return new Page<>(page, size);
        }

        /* 2. User 侧条件只拿 user_id */
        List<String> usrUserIds = userMapper.selectObjs(
                Wrappers.<User>lambdaQuery()
                        .select(User::getUserId)
                        .like(StringUtils.hasText(dto.getUid()),   User::getUid,   dto.getUid())
                        .like(StringUtils.hasText(dto.getName()),  User::getName,  dto.getName())
                        .like(StringUtils.hasText(dto.getPhone()), User::getPhone, dto.getPhone())
                        .like(StringUtils.hasText(dto.getRole()),  User::getRole,  dto.getRole())
                        .eq(dto.getIsActive() != null, User::getIsActive, dto.getIsActive())
        ).stream().map(String::valueOf).toList();

        if (usrUserIds.isEmpty()) {
            return new Page<>(page, size);
        }

        /* 3. 交集 */
        Set<String> intersect = new HashSet<>(admUserIds);
        intersect.retainAll(usrUserIds);
        if (intersect.isEmpty()) {
            return new Page<>(page, size);
        }

        /* 4. 真分页 */
        Page<Admin> adPage = new Page<>(page, size);
        Page<Admin> pageAd = adminMapper.selectPage(adPage,
                Wrappers.<Admin>lambdaQuery()
                        .in(Admin::getUserId, intersect)
                /* .orderByAsc/Desc(...) */
        );

        /* 5. 一次性补 User */
        List<String> pageUids = pageAd.getRecords()
                .stream()
                .map(Admin::getUserId)
                .toList();
        List<User> pageUsers = userMapper.selectList(
                Wrappers.<User>lambdaQuery().in(User::getUserId, pageUids));
        Map<String, User> userMap = pageUsers.stream()
                .collect(Collectors.toMap(User::getUserId, u -> u));

        /* 6. 组装 VO */
        List<AdminVo> records = pageAd.getRecords()
                .stream()
                .map(a -> toAdminVo(userMap.get(a.getUserId()), a))
                .toList();

        /* 7. 返回 */
        return new Page<AdminVo>(page, size)
                .setRecords(records)
                .setTotal(intersect.size());
    }

    public AdminVo toAdminVo(User u,Admin a){
        AdminVo vo = new AdminVo();
        vo.setBesides(a.getBesides());
        vo.setRealName(a.getRealName());
        vo.setEmail(a.getEmail());
        vo.setCreateBy(a.getCreateBy());
        vo.setUserId(u.getUserId());
        vo.setUid(u.getUid());
        vo.setName(u.getName());
        vo.setPhone(u.getPhone());
        vo.setRole(u.getRole());
        vo.setIsActive(u.getIsActive());
        vo.setImageUrl(u.getImageUrl());
        return vo;


    }
}
