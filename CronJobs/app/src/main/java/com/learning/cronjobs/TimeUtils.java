package com.learning.cronjobs;

import java.util.Map;
/**
 * Validation of time and calculation of the next scheduled time of the task
 */
public class TimeUtils {
    /**
     * For validate the time format as HH:MM
     * @param time -> curreent time
     * @return -> if time format is not as HH:MM then return false else true
     */
    public static boolean isValidTimeFormat(String time) {
        if(time.length() < 4){
            System.out.println("Error: Please provide time should be in HH:MM format, eg: 01:30");
        }else{
            String[] times=time.split(":");
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            if(isValidHourMin(hour, minute)) {
                return true;
            }
        }
        return false;
    }

    /**
     * For Validate the Hour and Minute
     * @param hour -> Hour value
     * @param minute -> minute value
     * @return -> If hour and minute is in range then it will return true else false
     */
    public static boolean isValidHourMin(int hour, int minute){
        int minTime = 00;
        int maxHour = 23;
        int maxMinute = 59;

        if (!((minTime <= hour && hour <= maxHour) && (minTime <= minute && minute <= maxMinute))){
            System.out.println("Error: out of bound for hours/minutes");
            return false;
        }else
            return true;
    }

    /**
     * For calculate the next execution time
     * @param crontabTime -> Time from cron commands
     * @param currentTime -> current time
     * @return -> next scheduled time
     */
    public static String timeDifferenceCalculation(Map<String,String> crontabTime, String currentTime) {
        String today = "today";
        String tomorrow = "tomorrow";
        int currentHour =0 , currentMin =0;
        if (currentTime.length() > 0 && currentTime.contains(":")) {
            String[] time = currentTime.split(":");
            currentHour = Integer.parseInt(time[0]);
            currentMin = Integer.parseInt(time[1]);
        }

        if (isNumeric(crontabTime.get("hour"))){
            int difference = (Integer.parseInt(crontabTime.get("hour")) - currentHour);
            if (difference < 0) {
                if (isNumeric(crontabTime.get("minute"))){
                    String result = crontabTime.get("hour")+" : "+crontabTime.get("minute")+" "+tomorrow;
                    return result;
                }else{
                    String result = crontabTime.get("hour")+" :00 "+tomorrow;
                    return result;
                }
            } else if (difference > 0) {
                if (isNumeric(crontabTime.get("minute"))) {
                    String result = crontabTime.get("hour")+" : "+crontabTime.get("minute")+" "+today;
                    return result;
                }else {
                    String result = crontabTime.get("hour")+" :00 "+today;
                    return result;
                }
            } else if (difference == 0) {
                if (isNumeric(crontabTime.get("minute"))) {
                    int minuteDifference = (Integer.parseInt(crontabTime.get("minute")) - currentMin);

                    if (minuteDifference == 0) {
                        String result = currentHour+" : "+currentMin+" "+today;
                        return result;
                    } else if (minuteDifference < 0) {
                        String result = currentHour+" : "+crontabTime.get("minute")+" "+tomorrow;
                        return result;
                    } else {
                        String result = currentHour+" : "+crontabTime.get("minute")+" "+today;
                        return result;
                    }
                } else if (!(isNumeric(crontabTime.get("minute"))) && (isNumeric(crontabTime.get("hour")))){
                    String result = crontabTime.get("hour")+" : "+currentMin+" "+today;
                    return result;
                }else{
                    String result = crontabTime.get("hour")+" :00 "+tomorrow;
                    return result;
                }
            }
        }else if(isNumeric(crontabTime.get("minute"))){
            int difference = (Integer.parseInt(crontabTime.get("minute")) - currentMin);

            if (difference > 0) {
                String result = currentHour+" : "+crontabTime.get("minute")+" "+today;
                return result;
            }else if(difference < 0) {
                if (currentHour < 23){
                    String hr = String.format("%02d", (currentHour+1));
                    String result = hr+" : "+crontabTime.get("minute")+" "+today;
                    return result;
                }else{
                    String result = "00 : "+crontabTime.get("minute")+" "+tomorrow;
                    return result;
                }
            }else {
                String result = currentHour+" : "+currentMin+" "+today;
                return result;
            }
        }else{
            String result = currentHour+" : "+currentMin+" "+today;
            return result;
        }
        return "";

    }

    /**
     * For validate the string is numeric or not
     * @param str -> String
     * @return -> true if passed string is digit else return false
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
