package com.hunt.worker-service-root;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestObjectJSONReader {

    public static <T> List<T> read(Class<T> beanClass, String fileName) {
        URL url = TestObjectJSONReader.class.getClassLoader().getResource(fileName);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            objectMapper.registerModule(javaTimeModule);

            return objectMapper.readValue(url, objectMapper.getTypeFactory().constructCollectionType(List.class, beanClass));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
