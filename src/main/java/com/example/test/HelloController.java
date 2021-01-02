package com.example.test;


import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 测试控制器
 */
@RestController //该注解是 @Controller 和 @ResponseBody 注解的合体版
public class HelloController {


}
//http://127.0.0.1:9000/something/project-file/c3dd825647ed4ca0b8c8fc105e7bc770.JPG