package com.kxindot.goblin.typeconvert.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author ZhaoQingJiang
 */
public class DateToLocalTimeConverter extends DateConverter<LocalTime> {

    @Override
    protected LocalTime convertValue(Date p) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(p.getTime()), 
                ZoneId.systemDefault())
                .toLocalTime();
    }

}
