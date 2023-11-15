package com.merge.doongG.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {
    // ModelMapper를 이용하기 위한 Bean 등록
    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}
