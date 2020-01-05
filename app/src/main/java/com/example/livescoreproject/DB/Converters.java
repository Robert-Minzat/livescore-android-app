package com.example.livescoreproject.DB;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String toTimestamp(Date value) {
        return df.format(value);
    }

//    @TypeConverter
//    public static Date fromTimestamp(Long timestamp) {
//        return timestamp != null ? new Date(timestamp)
//                : null;
//    }
//
//    @TypeConverter
//    public static Long fromDate(Date date) {
//        return date != null ? date.getTime()
//                : null;
//    }
}