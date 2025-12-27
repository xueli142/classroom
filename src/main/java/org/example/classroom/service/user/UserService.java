package org.example.classroom.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.classroom.dao.user.UserMapper;
import org.example.classroom.transfer.dto.login.ChangePasswordDto;
import org.example.classroom.transfer.dto.login.ChangeUsernameDto;
import org.example.classroom.transfer.dto.login.LoginDto;
import org.example.classroom.transfer.dto.login.LoginResponseDto;
import org.example.classroom.transfer.dto.selectDto.AdminSelectDto;
import org.example.classroom.transfer.dto.simpleVo.ChangePhoneDto;
import org.example.classroom.model.user.User;
import org.example.classroom.util.ImageUtil;
import org.example.classroom.util.JwtUtil;
import org.example.classroom.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class UserService {
@Resource
private UserMapper userMapper;
@Resource
private PasswordUtil passwordUtil;
@Resource
private JwtUtil jwtUtil;
@Resource
private ImageUtil imageUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${announcement.image-path}")
    private String accessUrlPrefix;



public LoginResponseDto login(LoginDto loginDto) {
    String uid = loginDto.getUid();

    User user = userMapper.selectUserByUid(uid);
   if(user==null||!passwordUtil.matches(loginDto.getPassword(),user.getPassword())){
       LoginResponseDto responseDto = new LoginResponseDto();
       responseDto.setMessage("账号不存在或密码错误");
       return responseDto;
   }
    String jwt =jwtUtil.generateToken(user.getUserId(),user.getRole());
    LoginResponseDto responseDto = new LoginResponseDto();
    responseDto.setToken(jwt);
    responseDto.setMessage("登录成功");
    responseDto.setUser(user);
    System.out.println(jwt);
return responseDto;


}
    public boolean changePassword(ChangePasswordDto dto) {
        User user = userMapper.selectUserByUid(dto.getUid());
        if (user == null || !passwordUtil.matches( dto.getOldPassword(),user.getPassword())) {
            return false;
        }
        user.setPassword(passwordUtil.encode(dto.getNewPassword()));
        return userMapper.updateByUserId(user);
    }

    public boolean changeUsername(ChangeUsernameDto dto) {
        User user = userMapper.selectUserByUid(dto.getUserId());
        if (user == null) {
            return false;
        }
        user.setUid(dto.getNewUid());
        return userMapper.updateByUserId(user);
    }


    public boolean changePhone(ChangePhoneDto dto) {
        User user = userMapper.selectUserByUid(dto.getUserId());
        if (user == null) {
            return false;
        }
        user.setPhone(dto.getPhone());
        return userMapper.updateByUserId(user);
    }

    //头像图片处理
    public Boolean saveImage (MultipartFile file,String userId){
        User user = userMapper.selectUserByUid(userId);
        if (user == null) {
            return false;
        }
    String url=imageUtil.saveImage(file);

    user.setImageUrl(url);
    return userMapper.updateByUserId(user);
    }

    public Boolean changeImage(MultipartFile file,String oldName,String userId){
        User user = userMapper.selectUserByUid(userId);
        if (user == null) {
            return false;
        }
        String url=imageUtil.changeImage(file,oldName);
        user.setImageUrl(url);
        return userMapper.updateByUserId(user);

    }

public Boolean  deleteByUserId(String userId){
    Boolean result=userMapper.deleteByUserId(userId);
    String url = userMapper.selectUrlByUserId(userId);

    if(url==null){
        return false;
    }
    return imageUtil.deleteImage(url)&&result;

}
    public Boolean deleteByUserIds(java.util.List<String> userIds){
        Boolean result1=userMapper.deleteByUserIds(userIds);
        List<String> urls =userMapper.selectUrlByUserIds(userIds);
        Boolean result2=imageUtil.deleteImages(urls);
        return result2&&result1;

}

//保留方法，对于user的分页查询
    public IPage<User> pageUser(AdminSelectDto dto) {
        long page = dto.getPage() == null ? 1 : dto.getPage();
        long size = dto.getSize() == null ? 10 : dto.getSize();

        Page<User> pg = new Page<>(page, size);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 姓名模糊
        wrapper.like(StringUtils.hasText(dto.getName()), User::getName, dto.getName());
        // 手机号模糊
        wrapper.like(StringUtils.hasText(dto.getPhone()), User::getPhone, dto.getPhone());
        // 角色精确
        wrapper.eq(StringUtils.hasText(dto.getRole()), User::getRole, dto.getRole());
        // uid 精确
        wrapper.eq(StringUtils.hasText(dto.getUid()), User::getUid, dto.getUid());
        // userId 精确
        wrapper.eq(StringUtils.hasText(dto.getUserId()), User::getUserId, dto.getUserId());
        // 状态精确
        wrapper.eq(dto.getIsActive() != null, User::getIsActive, dto.getIsActive());

        // 如果所有条件都为空，wrapper 将不包含任何 where 子句，直接走默认分页
        return userMapper.selectPage(pg, wrapper);
    }

}
