package com.ddchat_server.controller.resource;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ddchat_server.controller.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 文件控制器
 */
@RestController
@RequestMapping("/ddchat/file")
public class FileController {
    @Value("${path.file}")
    private String path;
    /**
     * 下载文件
     */
    @GetMapping("/download")
    @ResponseBody
    public String download(HttpServletRequest request, HttpServletResponse response) {
        String filePathName = path + request.getParameter("url");
        String fileRealName = request.getParameter("name");
        File file = new File(filePathName);
        if (!file.exists()) {
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"文件已经丢失！"));
        }
        //文件名字乱码问题
        try {
            fileRealName = new String(fileRealName.getBytes(StandardCharsets.UTF_8),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.reset();
        response.setContentType("multipart/x-download");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileRealName);
        response.addHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));

        try {
            InputStream inStream = new FileInputStream(filePathName);
            OutputStream os = response.getOutputStream();

            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            os.flush();
            os.close();

            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"文件下载出错！"));
        }

        return JSONUtil.toJsonStr(new ResponseDto<>("下载成功！"));
    }
    /**
     * 上传文件
     */
    @PostMapping(path = "/upload",consumes = "multipart/form-data")
    public String upload(MultipartFile file){

        String fileName = file.getOriginalFilename();
        if(null == fileName)
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"失败,文件没有名字"));
        String suffixName = fileName.substring(fileName.indexOf(".")).toLowerCase(Locale.ROOT);//后缀名

        ///给文件改名  更换成唯一id方便下载
        String download = UUID.randomUUID() + suffixName;
        File dest = new File(path + download);

        if(!dest.getParentFile().exists()){
            if(!dest.getParentFile().mkdirs())
                return JSONUtil.toJsonStr(new ResponseDto<>(false,"parent dir created failed"));
        }
        try {
            file.transferTo(dest);
        }catch (IOException e){
            e.printStackTrace();
            return JSONUtil.toJsonStr(new ResponseDto<>(false,"upload failed..."));
        }

        JSONObject rs = new JSONObject();
        rs.put("name",fileName);
        rs.put("download",download);
        rs.put("size",String.format("%.4f", file.getSize()/1048576.0)+"MB");
        return JSONUtil.toJsonStr(new ResponseDto<>(rs));
    }

}
