package com.leyou.common.leyougateway.com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class LeyouCorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        //添加CORS配置信息
        CorsConfiguration config=new CorsConfiguration();
        //允许的域，不要写*，否则cookie无法使用
        config.addAllowedOrigin("http://manage.leyou.com");

        //是否发送cookie信息
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");

        //允许的头信息
        config.addAllowedHeader("*");
        //添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource configsource= new UrlBasedCorsConfigurationSource();
        configsource.registerCorsConfiguration("/**",config);
            //3.返回新的CorsFilter.
        return new CorsFilter(configsource);
    }
}
