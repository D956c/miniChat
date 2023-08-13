package com.ddchat_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${path.image}")
    String imagePath;
    @Value("${path.file}")
    String filePath;
    @Value("${path.video}")
    String videoPath;
    @Value("${path.audio}")
    String audioPath;
    // 当前跨域请求最大有效时长。这里默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    /**
     * 图片和文件的物理地址
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resource/image/**").addResourceLocations("file:"+imagePath);
        registry.addResourceHandler("/resource/file/**").addResourceLocations("file:"+filePath);
        registry.addResourceHandler("/resource/audio/**").addResourceLocations("file:"+audioPath);
        registry.addResourceHandler("/resource/video/**").addResourceLocations("file:"+videoPath);
    }

    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("*");
        //2) 是否发送Cookie信息
        config.setAllowCredentials(true);
        //3) 允许的请求方式GET POST等
        config.addAllowedMethod("*");
        // 4）允许的头信息
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}



