package com.learn.oss.controller;

import com.learn.common.result.R;
import com.learn.oss.service.OssService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/oss")
public class OSSController {
    @Resource
    OssService ossService ;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(MultipartFile file , String module){
        String imgUrl = ossService.upload(file,module);
        return R.ok().data("path",imgUrl);
    }

    @ApiOperation("文件删除")
    @DeleteMapping("delete")
    public R deleteFile(@RequestParam("path")String path){
        ossService.deleteFile(path);
        return R.ok().message("文件删除成功");
    }
}
