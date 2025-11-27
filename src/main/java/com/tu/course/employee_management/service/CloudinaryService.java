package com.tu.course.employee_management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.tu.course.employee_management.exception.CloudinaryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {
            Map uploadParams = ObjectUtils.asMap(
                    "upload_preset", "spring-users",
                    "quality", "auto",           // automatic compression
                    "fetch_format", "auto"       // convert to WebP/AVIF when supported
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return (String) uploadResult.get("public_id");
        } catch (Exception e) {
            throw new CloudinaryException(CloudinaryException.Operation.UPLOAD, e);
        }
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new CloudinaryException(CloudinaryException.Operation.DELETE, e);
        }
    }

    public String generateUrl(String publicId, Integer width, Integer height) {
        return cloudinary.url()
                .transformation(new Transformation().width(width).height(height).crop("fill"))
                .generate(publicId);
    }

    // Delete all images in a Cloudinary folder
    public void deleteAllImagesInFolder(String folderName) {
        try {
            // Get the resource context
            ApiResponse result = cloudinary.api().resources(ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", folderName // Filter by folder name
            ));

            // Get the list of images in the folder
            List<Map<String, Object>> resources = (List<Map<String, Object>>) result.get("resources");

            // Loop through and delete each image
            for (Map<String, Object> resource : resources) {
                String publicId = (String) resource.get("public_id");
                deleteImage(publicId);
                System.out.println("Deleted image: " + publicId);
            }
        } catch (Exception e) {
            throw new CloudinaryException(CloudinaryException.Operation.DELETE, e);
        }
    }

}
