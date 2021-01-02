package com.example.test.controller;

import com.example.test.service.MinioService;
import com.example.test.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    public MinioService minioService;

    @PostMapping("/upload")
    public AjaxResult uploadPic(MultipartFile file){
        return minioService.minioUpload(file);
    }

    @DeleteMapping("/delete")
    public AjaxResult deletePic(String name){
        return minioService.minioDelete(name);
    }

    @RequestMapping("/visit")
    public AjaxResult visitPic(HttpServletResponse response)throws Exception{
        return minioService.minioVisit(response);
    }
}
