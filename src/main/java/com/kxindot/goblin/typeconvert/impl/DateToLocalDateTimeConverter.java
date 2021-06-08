package com.kxindot.goblin.typeconvert.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author ZhaoQingJiang
 */
public class DateToLocalDateTimeConverter extends DateConverter<LocalDateTime> {

    @Override
    protected LocalDateTime convertValue(Date p) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(p.getTime()), 
                ZoneId.systemDefault());
    }

}
