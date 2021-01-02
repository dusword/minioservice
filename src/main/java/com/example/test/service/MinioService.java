package com.example.test.service;

import com.example.test.utils.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface MinioService {


    AjaxResult minioUpload(MultipartFile file);
    AjaxResult minioDelete(String name);
    AjaxResult minioVisit(HttpServletResponse response) throws Exception;


}
