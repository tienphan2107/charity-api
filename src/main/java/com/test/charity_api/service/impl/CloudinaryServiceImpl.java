package com.test.charity_api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.test.charity_api.service.CloudinaryService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinaryConfig;

    public CloudinaryServiceImpl(Cloudinary cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            File uploadedFile = convertMultiPartToFile(file);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.asMap(
                    "asset_folder", "charity",
                    "resource_type", "image"));
            uploadedFile.delete();
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @Override
    public void delete(String publicId) {
        try {
            cloudinaryConfig.uploader().destroy(publicId, ObjectUtils.asMap("asset_folder", "charity"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
