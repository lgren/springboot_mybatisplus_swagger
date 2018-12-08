package com.lgren.springboot_mybatisplus_swagger;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.lgren.springboot_mybatisplus_swagger.mapper")
@EnableCaching
public class SpringbootMybatisplusSwaggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisplusSwaggerApplication.class, args);
	}

    @Bean
    public HttpMessageConverters fastJsonConfigure(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,// 是否输出值为null的字段,默认为false
                SerializerFeature.WriteNullStringAsEmpty,// 字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullListAsEmpty,// List字段如果为null,输出为[],而非null
                SerializerFeature.WriteDateUseDateFormat,// 全局修改日期格式,默认为false。JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd"
                SerializerFeature.WriteEnumUsingToString);// Enum输出name()或者original,默认为false
        converter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> supportedMediaTypes = new ArrayList<>(2);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        return new HttpMessageConverters(converter);
    }
}
