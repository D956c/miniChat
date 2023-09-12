package com.ddchat_server.controller.resource;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ddchat_server.controller.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/ddchat/video")
public class VideoController {
    @Value("${path.video}")
    private String path;

    /**
     * 上传图片
     */
    @PostMapping(path = "/upload",consumes = "multipart/form-data")
    public String upload(MultipartFile video){
        String fileName = video.getOriginalFilename();
        if(null == fileName)
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"找不到视频名字"));
        int index = fileName.lastIndexOf(".");
        if(index==-1){
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"找不到视频格式！"));
        }
        String suffixName = fileName.substring(index).toLowerCase(Locale.ROOT);//后缀名
        ///给视频改名
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(path + fileName);

        if(!dest.getParentFile().exists()){
            if(!dest.getParentFile().mkdirs())
                return JSONUtil.toJsonStr(new ResponseDto<>(false,"parent dir created failed"));
        }
        try {
            video.transferTo(dest);
        }catch (IOException e){
            e.printStackTrace();
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"upload failed..."));
        }

        JSONObject rs = new JSONObject();
        rs.put("url","resource/video/"+fileName);
        return JSONUtil.toJsonStr(new ResponseDto<>(rs));
    }
}
