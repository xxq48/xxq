package cn.edu.qjnu.xxq.controller;

import  cn.edu.qjnu.xxq.util.ElResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/upload")

public class UploadController {

    @RequestMapping("/img")
    public ElResult uploadImg(MultipartFile file) throws IOException {
        // 改名为随机字符串命名
        String originalFilename = file.getOriginalFilename(); //获取原文件名
        //取文件的扩展名
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 获取一个随机字符串
        String s = UUID.randomUUID().toString();
        String newFileName = s + "." + extName;
        //保存到目录
        file.transferTo(new File("C:/Users/X/Documents/IdeaProjects" + newFileName));

        //返回URL
        return ElResult.error("http://localhost:8080/image/" + newFileName);
    }
}