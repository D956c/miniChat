package com.ddchat_server.filter;

import cn.hutool.json.JSONObject;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.util.JWTUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

//过滤器
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //System.out.println("---------您已进入过滤器----------");
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse)servletResponse);
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if(httpServletRequest.getMethod().equals("OPTIONS")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        try {
//            String token = httpServletRequest.getHeader("token");
//            System.out.println("拿到的token"+token);
//            if(!JWTUtils.verify(token)) throw new Exception();
        }catch (Exception e){
            // 获得response
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            // 服务器允许发送cookie
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            // 允许接收请求的域名
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            // 设置UTF-8编码
            httpResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json;charset=utf-8");
            // 将数据写回浏览器
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", false).put("message", "身份验证失效").put("code", "E1");
            httpResponse.getWriter().println(jsonObject);
            return;
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}