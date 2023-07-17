package br.com.mundim.CarRent.utils;

import java.text.DecimalFormat;

public class NumberToString {

    public static String doubleToString(Double value, int decimalPlaces) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(decimalPlaces);
        df.setMinimumFractionDigits(decimalPlaces);

        return df.format(value);
    }

}
