package com.learning.cronjobs;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.*;

/**
 * This class job is get the next scheduled time of the task
 */
public class CronJobs {

  public static void main(String[] args)
  {
    Scanner scanner= new Scanner(System.in);
    System.out.print("Please input the current time as HH:MM - ");
    String currenTime= scanner.nextLine();              //reads string
    System.out.println("You have entered: "+currenTime);
    if(currenTime.length() < 0){
      System.out.println("Error: Please input the current time as HH:MM");
    }else{
      boolean isValidCurrentTime = TimeUtils.isValidTimeFormat(currenTime);

      if(isValidCurrentTime) {
        //To enter the file path from standard input, uncomment the below code
        //System.out.println("Enter file path as string - ");
        //String file= scanner.nextLine();              //reads string
        //System.out.println("You have entered file path: "+file);

        Path path = Paths.get("app/src/main/java/com/learning/cronjobs/Config.txt");
        String file = path.toAbsolutePath().toString();

        if(CronTabParser.isValidFilePath(file)) {
            ArrayList<String> crontabList = CronTabParser.parseConfig(file);
            CronTabParser.nextCrontabTime(currenTime, crontabList);
        }else{
            System.out.println("Error: Provide the correct file path");
        }
      }
    }
  }

}




