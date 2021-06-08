package com.kxindot.goblin.typeconvert.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author ZhaoQingJiang
 */
public class DateToLocalDateConverter extends DateConverter<LocalDate> {

    @Override
    protected LocalDate convertValue(Date p) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(p.getTime()), 
                ZoneId.systemDefault())
                .toLocalDate();
    }

}
