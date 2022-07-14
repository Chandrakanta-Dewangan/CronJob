package com.learning.cronjobs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;

/**
 * Read the configuration file and validate the cron commands
 */
public class CronTabParser {

    /**
     * for check the valid path of the file
     * @param path
     * @return
     */
    public static boolean isValidFilePath(String path) {
        File f = new File(path);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    /**
     * For read the file
     */
    public static ArrayList<String> parseConfig(String filename) {
        ArrayList<String> crontabList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new FileReader(filename));
            while (scanner.hasNext()) {
                String str = scanner.nextLine();
                String[] lines = str.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    crontabList.add(lines[i]);
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return crontabList;
    }

    /**
     * For get the next scheduled task time
     * @param currentTime
     * @param crontabList
     */
    public static void nextCrontabTime(String currentTime, ArrayList<String> crontabList) {
        for(String cronList : crontabList) {
            String[] lines = cronList.replaceAll("\\r|\\n", "").split(" ");
            String crontabMinute = lines[0];
            String crontabHour = lines[1];
            String crontabCommand = lines[2];

            Map<String, String> crontabValidatedTime = crontabTimeValidator(crontabList, crontabMinute, crontabHour);
            if (crontabValidatedTime.size() > 0) {
                String result = TimeUtils.timeDifferenceCalculation(crontabValidatedTime, currentTime);
                System.out.println(result + " - " + crontabCommand);
            }
        }
    }

    /**
     * For Validate the cron commands
     * @param cronTab
     * @param crontabMinute
     * @param crontabHour
     * @return
     */
    public static Map<String, String> crontabTimeValidator(ArrayList<String> cronTab,String crontabMinute, String crontabHour) {
        Map<String,String> result = new HashMap<String, String>();
        for(String cron : cronTab){
            if(!(cron.contains("*")) && !( cron.matches(".*[0-9].*"))){
                System.out.println("Incorrect format of cron commands");
            }
        }

        if(TimeUtils.isNumeric(crontabHour) && TimeUtils.isNumeric(crontabMinute)){
            TimeUtils.isValidHourMin(Integer.parseInt(crontabHour), Integer.parseInt(crontabMinute));
        }else if(TimeUtils.isNumeric(crontabHour)){
            TimeUtils.isValidHourMin(Integer.parseInt(crontabHour), 0);
        }else if(TimeUtils.isNumeric(crontabMinute)){
            TimeUtils.isValidHourMin(0, Integer.parseInt(crontabMinute));
        }
        result.put("hour",crontabHour);
        result.put("minute",crontabMinute);
        return result;
    }
}
