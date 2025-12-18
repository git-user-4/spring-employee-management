package com.tu.course.employee_management.startup;

import com.tu.course.employee_management.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class CloudinaryCleanupRunner implements CommandLineRunner {

    private final CloudinaryService cloudinaryService;

    @Override
    public void run(String... args) throws Exception {
        String folderName = "spring-user-avatars";
        cloudinaryService.deleteAllImagesInFolder(folderName);
        log.info("--- Deleted all Cloudinary images inside {}! ---", folderName);
    }

}
