package br.com.mundim.CarRent.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateToString {

    public static String localDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        return dateTime.format(formatter);
    }

}
