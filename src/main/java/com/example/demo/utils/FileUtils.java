package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    @Value("${file.upload-dir}")
    static String FILE_DIRECTORY;

    public static void uploadFiletoS3(MultipartFile file) throws IOException {
        File myFile = new File(FILE_DIRECTORY+file.getOriginalFilename());
        myFile.createNewFile();
        FileOutputStream fos =new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        myFile.getAbsolutePath();
    }
}
