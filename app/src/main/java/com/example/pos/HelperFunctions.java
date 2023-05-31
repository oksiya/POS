package com.example.pos;


import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HelperFunctions {

    public static double stringToDouble(String str){
        double dbl = 0.0;
        try {
            if(str == null){
                str = "0.0";
                double tempDbl = Double.parseDouble(str);
                dbl = Math.round(tempDbl * 100.0) / 100.0;
            }else {
                double tempDbl = Double.parseDouble(str);
                dbl = Math.round(tempDbl * 100.0) / 100.0;
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return dbl;
    }

    public static int stringToInteger(String str){
        int num = 0;
        try {
            num = Integer.parseInt(str);

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return num;
    }

    public static String doubleToString(double dbl){
        String str = null;
        try {
            str = Double.toString(dbl);

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return str;
    }

    public static double sum(ArrayList<Double> money){

        double total = 0.0;
        for (double dbl:
             money) {
            total = total + dbl;
        }
        return total;
    }

    public static String roundToTwoDecimalPlaces(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(value);
    }

    public static String intToString(int integer){
        String str = null;
        try {
            str = Integer.toString(integer);

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return str;
    }

    public static String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat();
        Date currentDate = new Date();
        return formatter.format(currentDate);
    }
}