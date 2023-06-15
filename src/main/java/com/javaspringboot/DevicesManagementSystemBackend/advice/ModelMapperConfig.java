package com.javaspringboot.DevicesManagementSystemBackend.advice;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Service
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//        return builder -> {
//            builder.simpleDateFormat(dateFormat);
//            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
//            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
//        };
//    }



}
