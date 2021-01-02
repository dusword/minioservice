package com.example.test.controller;

import com.example.test.service.MinioService;
import com.example.test.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
public class MinioCon {

    @Autowired
    private MinioUtil minioUtil;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    private MinioService minioService;

    /**
     * 创建储存桶
     * @param bucketName
     */
    public void CreateBucket(String bucketName){

        /*policy 的设定可以参照aws 中文档说明*/
        /*https://docs.aws.amazon.com/AmazonS3/latest/dev/access-policy-language-overview.html*/
        StringBuilder policyJsonBuilder = new StringBuilder();
        policyJsonBuilder.append("{\n");
        policyJsonBuilder.append("    \"Statement\": [\n");
        policyJsonBuilder.append("        {\n");
        policyJsonBuilder.append("            \"Action\": [\n");
        policyJsonBuilder.append("                \"s3:GetBucketLocation\",\n");
        policyJsonBuilder.append("                \"s3:ListBucket\"\n");
        policyJsonBuilder.append("            ],\n");
        policyJsonBuilder.append("            \"Effect\": \"Allow\",\n");
        policyJsonBuilder.append("            \"Principal\": \"*\",\n");
        policyJsonBuilder.append("            \"Resource\": \"arn:aws:s3:::"+bucketName+"\"\n");
        policyJsonBuilder.append("        },\n");
        policyJsonBuilder.append("        {\n");
        policyJsonBuilder.append("            \"Action\": \"s3:GetObject\",\n");
        policyJsonBuilder.append("            \"Effect\": \"Allow\",\n");
        policyJsonBuilder.append("            \"Principal\": \"*\",\n");
        policyJsonBuilder.append("            \"Resource\": \"arn:aws:s3:::"+bucketName+"/project-file*\"\n");
        policyJsonBuilder.append("        }\n");
        policyJsonBuilder.append("    ],\n");
        policyJsonBuilder.append("    \"Version\": \"2012-10-17\"\n");
        policyJsonBuilder.append("}\n");

        try {
            minioUtil.makeBucket(bucketName);
            minioUtil.setBucketPolicy(bucketName, policyJsonBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建存储桶失败：" + e.getMessage());
        }
    }



    /**
     * 文件上传接口
     * @param file  上传的文件对象
     * @return  上传成功返回文件url 图片  txt 文件支持通过url 浏览器直接预览
     */
//    @PostMapping("/upload")
//    public String MinIOUpload(MultipartFile file) {
//
//        if (file.isEmpty() || file.getSize() == 0) {
//            return "文件为空";
//        }
//        try {
//            if (!minioUtil.bucketExists(bucketName)) {
//
//                CreateBucket(bucketName);
//            }
//
//            String fileName = file.getOriginalFilename();
//            String newName = "project-file/" + UUID.randomUUID().toString().replaceAll("-", "")
//                    + fileName.substring(fileName.lastIndexOf("."));
//
//            InputStream inputStream = file.getInputStream();
//            minioUtil.putObject(bucketName, newName, inputStream, file.getContentType());
//
//            inputStream.close();
//
//            String url = minioUtil.getObjectUrl(bucketName, newName);
//            return url;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "上传失败";
//        }
//    }

    /**
     * 文件下载
     * @param filename   文件名
     * @param httpResponse 接口返回对象
     */
    @GetMapping("download/{filename}")
    public void downloadFiles(@PathVariable("filename") String filename, HttpServletResponse httpResponse) {

        try {
            InputStream object = minioUtil.getObject(bucketName, "/project-file/"+filename);
            byte buf[] = new byte[1024];
            int length = 0;
            httpResponse.reset();
            httpResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            httpResponse.setContentType("application/octet-stream");
            httpResponse.setCharacterEncoding("utf-8");
            OutputStream outputStream = httpResponse.getOutputStream();
            while ((length = object.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            outputStream.close();
        } catch (Exception ex) {
            System.out.println("文件下载失败：" + ex.getMessage());
        }
    }

//    @DeleteMapping("/delete")
//    public String delete(String name) {
//        try {
//            minioUtil.removeObject(bucketName,name);
//        } catch (Exception e) {
//            return "删除失败"+e.getMessage();
//        }
//        return "删除成功";
//    }

//    @RequestMapping("/hello")
//    final public void testRed(HttpServletResponse response) throws Exception{
//        response.sendRedirect("http://127.0.0.1:9000/something/project-file/c3dd825647ed4ca0b8c8fc105e7bc770.JPG");
//    }

//    @RequestMapping("/visitPic")
//    public void ShowPic(){
//
//    }
    @GetMapping("/show")
    public String show(String name){
        try {
            return minioUtil.getObjectUrl(bucketName, name);
        }catch (Exception e){
            return "获取失败"+e.getMessage();
        }
    }


}

