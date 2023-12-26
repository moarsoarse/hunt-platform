package com.hunt.bpm-launcher-service.business.common.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DateTimeConverterTest {

    @InjectMocks
    private DateTimeConverter dateTimeConverter;

    @Test
    public void toDate() {
        //GIVEN
        LocalDateTime localDateTime = LocalDateTime.of(2016, 10, 3, 12, 3, 31);

        //WHEN
        Date date = DateTimeConverter.toDate(localDateTime);

        //THEN
        assertEquals("2016-10-03 12:03:31", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date));
    }
}