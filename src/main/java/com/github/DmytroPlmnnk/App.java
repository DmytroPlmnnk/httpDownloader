package com.github.DmytroPlmnnk;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        List<String> fileUrls = new ArrayList<>();

        System.out.println("Enter file URL to add to the downloads or enter 1 to start downloading added files: ");
        boolean continueAdding = true;
        String input;

        while (true){
            input = scanner.nextLine();
            if (input.equals("1")){
                break;
            }
            fileUrls.add(input);
        }
        System.out.println("Please enter the path to download the files (example - C:\\Users\\Downloads\\) : ");
        String outputPath = scanner.nextLine();
        scanner.close();



        Downloader downloader = new Downloader(fileUrls);
        downloader.downloadAll(outputPath);



    }

}
