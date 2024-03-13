package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/*
* 通用接口
* */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommomController {


    //本地存储
    @PutMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}",file);

        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + substring;
            log.info("新文件名：{}",objectName);
            file.transferTo(new File("E:\\JAVA\\image\\"+objectName));
            //云
            //文件请求路径,上传阿里云oss需要.
            //String filePath = aliOssUtil.upload(file.getBytes(), objectName);

        } catch (IOException e) {
            log.error("文件上传失败:{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);

        //云存储
//    @Autowired
//    private AliOssUtil aliOssUtil;
//    @PutMapping("/upload")
//    @ApiOperation("文件上传")
//    public Result<String> upload(MultipartFile file){
//        log.info("文件上传:{}",file);
//        try {
//            String originalFilename = file.getOriginalFilename();
//            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
//            String objectName = UUID.randomUUID().toString() + substring;
//            //文件请求路径
//            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
//        } catch (IOException e) {
//            log.error("文件上传失败:{}",e);
//        }
//        return Result.error(MessageConstant.UPLOAD_FAILED);
//    }
}
}


