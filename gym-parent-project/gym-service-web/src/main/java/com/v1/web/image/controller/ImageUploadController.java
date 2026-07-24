package com.v1.web.image.controller;

import com.alibaba.fastjson.JSONObject;
import com.v1.config.MinioUtils;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/upload")
public class ImageUploadController {

    @Autowired
    MinioUtils minioUtils;

    @PostMapping("/uploadImage")
    public ResultVo uploadImage(@RequestParam("file")MultipartFile file){
        log.info("上传图片接口层触发");
        JSONObject resource = null;
        try{
            resource =  minioUtils.uploadFile(file,"gym");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultUtils.success("上传成功",resource);
    }

}
