package com.ddchat_server.controller.resource;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.util.AffineTransImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/ddchat/image")
public class ImageController {
    @Value("${path.image}")
    private String path;

    /**
     * 上传图片
     */
    @PostMapping(path = "/upload",consumes = "multipart/form-data")
    public String upload(MultipartFile image){

        String fileName = image.getOriginalFilename();
        if(null == fileName)
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"找不到图片名字"));
        int index = fileName.lastIndexOf(".");
        if(index==-1){
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"找不到图片格式！"));
        }
        String suffixName = fileName.substring(index).toLowerCase(Locale.ROOT);//后缀名
        ///给图片改名
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(path + fileName);

        if(!dest.getParentFile().exists()){
            if(!dest.getParentFile().mkdirs())
                return JSONUtil.toJsonStr(new ResponseDto<>(false,"parent dir created failed"));
        }
        try {
            image.transferTo(dest);
        }catch (IOException e){
            e.printStackTrace();
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"upload failed..."));
        }

        JSONObject rs = new JSONObject();
        //创建缩略图
        if(image.getSize()>1024*500){
            AffineTransImage.minImage(path,fileName);
            rs.put("compressUrl", "resource/image/min_"+fileName);
        }else{
            rs.put("compressUrl", "resource/image/"+fileName);
        }
        rs.put("url","resource/image/"+fileName);
        return JSONUtil.toJsonStr(new ResponseDto<>(rs));
    }

    /**
     * 上传头像
     */
    @PostMapping(path = "/uploadAvatar",consumes = "multipart/form-data")
    public String uploadAvatar(MultipartFile image,String userId){
        String fileName = image.getOriginalFilename();
        if(null == fileName)
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"找不到图片名字"));
        int index = fileName.lastIndexOf(".");
        if(index==-1){
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"找不到图片格式！"));
        }
        String suffixName = fileName.substring(index).toLowerCase(Locale.ROOT);//后缀名
        ///给图片改名
        fileName = "avatar-of-user-"+userId+ suffixName;
        File dest = new File(path + fileName);

        if(!dest.getParentFile().exists()){
            if(!dest.getParentFile().mkdirs())
                return JSONUtil.toJsonStr(new ResponseDto<>(false,"parent dir created failed"));
        }
        try {
            image.transferTo(dest);
        }catch (IOException e){
            e.printStackTrace();
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"upload failed..."));
        }

        JSONObject rs = new JSONObject();
        //创建缩略图
        if(image.getSize()>1024*500){
            AffineTransImage.minImage(path,fileName);
            rs.put("compressUrl", "resource/image/min_"+fileName);
        }else{
            rs.put("compressUrl", "resource/image/"+fileName);
        }
        rs.put("url","resource/image/"+fileName);
        return JSONUtil.toJsonStr(new ResponseDto<>(rs));
    }
}
