package org.example.classroom.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Component
public class ImageUtil {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${announcement.image-path}")
    private String accessUrlPrefix;


    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("空文件");
        }

        // 1. 校验真实格式
        String contentType = file.getContentType();
        if (!Arrays.asList("image/jpeg", "image/png", "image/webp","gif").contains(contentType)) {
            throw new IllegalArgumentException("仅支持 jpg/png/webp");
        }

        // 2. 安全扩展名
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());


        // 3. 唯一文件名
        String newName = null;
        if (ext != null) {
            newName = IdGeneratorUtil.generateUuid() + "." + ext.toLowerCase();
        }

        // 4. 确保目录存在
        Path folder = Paths.get(uploadDir);
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            throw new RuntimeException("创建目录失败", e);
        }

        // 5. 落盘（StandardCopyOption.REPLACE_EXISTING 按需）
        Path target = folder.resolve(newName);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("保存文件失败", e);
        }

        // 6. 返回访问地址
        return accessUrlPrefix + newName;
    }

    public String changeImage(MultipartFile file, String oldName) {
        if (file.isEmpty()) throw new IllegalArgumentException("空文件");

        /* 1. 从 URL 里把“文件名”切出来 */
        String fileName;
        try {
            fileName = Paths.get(new URL(oldName).getPath()).getFileName().toString();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("oldName 不是合法 URL", e);
        }

        /* 2. 校验扩展名（用新文件的真实扩展名） */
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!Arrays.asList("jpg", "jpeg", "png", "webp").contains(ext.toLowerCase()))
            throw new IllegalArgumentException("非法类型");

        /* 3. 删旧图（用文件名） */
        Path oldPath = Paths.get(uploadDir, fileName);
        try {
            Files.deleteIfExists(oldPath);
        } catch (IOException e) {
            throw new RuntimeException("删除旧图失败", e);
        }

        /* 4. 写新图（文件名保持原来的，避免数据库更新） */
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, oldPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("替换失败", e);
        }

        /* 5. 返回原 URL（不变） */
        return accessUrlPrefix + fileName;
    }


    public Boolean deleteImage(String imageUrl) {
        try {
            // 从 URL 中解析出文件名
            String fileName = Paths.get(new URL(imageUrl).getPath()).getFileName().toString();

            // 构建文件的完整路径
            Path filePath = Paths.get(uploadDir, fileName);

            // 检查文件是否存在
            if (Files.exists(filePath)) {
                // 删除文件
                Files.delete(filePath);
                return true; // 删除成功，返回 true
            } else {
                // 文件不存在，返回 false
                return false;
            }
        } catch (MalformedURLException e) {
            // URL 格式错误
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // 捕获异常，例如文件正在使用或没有权限删除
            e.printStackTrace();
            return false;
        }
    }
    public Boolean deleteImages(List<String> imageUrls){
        for(String imageUrl:imageUrls){
            if(!deleteImage(imageUrl)){
                return false;
            }
        }
        return true;
    }
}
