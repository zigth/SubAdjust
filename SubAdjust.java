import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.regex.*;

//import java.lang.*;

public class SubAdjust {
    public static long[] stringToNumsSrt(String times){
        long[] result = new long[2];
        int hours = Integer.parseInt(times.substring(0,2));
        int minutes = Integer.parseInt(times.substring(3,5));
        int seconds = Integer.parseInt(times.substring(6,8));
        int milliseconds = Integer.parseInt(times.substring(9,12));

        result[0] = hours*3600000+minutes*60000+seconds*1000+milliseconds;

        hours = Integer.parseInt(times.substring(17,19));
        minutes = Integer.parseInt(times.substring(20,22));
        seconds = Integer.parseInt(times.substring(23,25));
        milliseconds = Integer.parseInt(times.substring(26,29));

        result[1] = hours*3600000+minutes*60000+seconds*1000+milliseconds;

        return result;
    }

    /*function for transforming integers to srt style strings*/
    public static String numsToStringSrt(long time1, long time2){
        long milliseconds1 = time1%1000;
        time1 -= milliseconds1;
        long seconds1 = time1%60000;
        time1 -= seconds1;
        seconds1 /= 1000;
        long minutes1 = time1%3600000;
        time1 -= minutes1;
        minutes1 /= 60000;
        long hours1 = time1/3600000;

        long milliseconds2 = time2%1000;
        time2 -= milliseconds2;
        long seconds2 = time2%60000;
        time2 -= seconds2;
        seconds2 /= 1000;
        long minutes2 = time2%3600000;
        time2 -= minutes2;
        minutes2 /= 60000;
        long hours2 = time2/3600000;


        String shours1 = String.format("%02d",hours1);
        String sminutes1 = String.format("%02d",minutes1);
        String sseconds1 = String.format("%02d",seconds1);
        String smilliseconds1 = String.format("%03d",milliseconds1);

        String shours2 = String.format("%02d",hours2);
        String sminutes2 = String.format("%02d",minutes2);
        String sseconds2 = String.format("%02d",seconds2);
        String smilliseconds2 = String.format("%03d",milliseconds2);

        String result = shours1+":"+sminutes1+":"+sseconds1+","+smilliseconds1+" --> "+shours2+":"+sminutes2+":"+sseconds2+","+smilliseconds2;

        return result;
    }

    /*function for transforming srt style strings into integers*/
    public static long timeToNumSrt(String time){
        int hours=Integer.parseInt(time.substring(0,2));
        int minutes=Integer.parseInt(time.substring(3,5));
        int seconds=Integer.parseInt(time.substring(6,8));

        long result=hours*3600000+minutes*60000+seconds*1000;

        return result;
    }


    /*function for transforming srt style strings into integers*/
    public static long[] stringToNumsAss(String times){
        long[] result = new long[2];
        int hours = Integer.parseInt(times.substring(0,1));
        int minutes = Integer.parseInt(times.substring(2,4));
        int seconds = Integer.parseInt(times.substring(5,7));
        int centiseconds = Integer.parseInt(times.substring(8,10));

        result[0] = hours*360000+minutes*6000+seconds*100+centiseconds;

        hours = Integer.parseInt(times.substring(11,12));
        minutes = Integer.parseInt(times.substring(13,15));
        seconds = Integer.parseInt(times.substring(16,18));
        centiseconds = Integer.parseInt(times.substring(19,21));

        result[1] = hours*360000+minutes*6000+seconds*100+centiseconds;

        return result;
    }

    /*function for transforming integers to srt style strings*/
    public static String numsToStringAss(long time1, long time2){
        long centiseconds1 = time1%100;
        time1 -= centiseconds1;
        long seconds1 = time1%6000;
        time1 -= seconds1;
        seconds1 /= 100;
        long minutes1 = time1%360000;
        time1 -= minutes1;
        minutes1 /= 6000;
        long hours1 = time1/360000;

        long centiseconds2 = time2%100;
        time2 -= centiseconds2;
        long seconds2 = time2%6000;
        time2 -= seconds2;
        seconds2 /= 100;
        long minutes2 = time2%360000;
        time2 -= minutes2;
        minutes2 /= 6000;
        long hours2 = time2/360000;


        String shours1 = String.format("%01d",hours1);
        String sminutes1 = String.format("%02d",minutes1);
        String sseconds1 = String.format("%02d",seconds1);
        String scentiseconds1 = String.format("%02d",centiseconds1);

        String shours2 = String.format("%01d",hours2);
        String sminutes2 = String.format("%02d",minutes2);
        String sseconds2 = String.format("%02d",seconds2);
        String scentiseconds2 = String.format("%02d",centiseconds2);

        String result = shours1+":"+sminutes1+":"+sseconds1+"."+scentiseconds1+","+shours2+":"+sminutes2+":"+sseconds2+"."+scentiseconds2;

        return result;
    }

    /*function for transforming srt style strings into integers*/
    public static long timeToNumAss(String time){
        int hours=Integer.parseInt(time.substring(0,2));
        int minutes=Integer.parseInt(time.substring(3,5));
        int seconds=Integer.parseInt(time.substring(6,8));

        long result=hours*360000+minutes*6000+seconds*100;

        return result;
    }




    public static void main(String[] args) throws IOException {
        /*initialisation of input+output stream*/
        Scanner input = new Scanner(System.in);
        System.out.print("Name of the subtitle file: ");
        String inputFileName = input.nextLine().trim();

        boolean invalidFileExtension = true;
        boolean fileType = true;

        while (invalidFileExtension) {


            String fileExtension = inputFileName.substring(inputFileName.length() - 3);
            switch (fileExtension) {
                case "srt":
                    fileType = true;
                    invalidFileExtension = false;
                    break;
                case "ass":
                    fileType = false;
                    invalidFileExtension = false;
                    break;
                default:
                    System.out.print("Invalid file extension try again: ");
                    inputFileName = input.nextLine().trim();
            }
        }

        String outputFileName;
        if (fileType) {
            outputFileName = inputFileName.substring(0, inputFileName.length() - 4).concat("2.srt");
        }else{
            outputFileName = inputFileName.substring(0, inputFileName.length() - 4).concat("2.ass");
        }

        /*determination of the operations to be used*/
        System.out.print("Apply linear adjustment? (y/n): ");
        char conditionChar = input.nextLine().trim().charAt(0);
        boolean additionCondition=(conditionChar=='y');

        long startTime=0;
        int addConst=0;
        Pattern timepattern;
        if (fileType){
            timepattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d[,.]\\d\\d\\d.....\\d\\d:\\d\\d:\\d\\d[,.]\\d\\d\\d");
            //00:00:47,840 --> 00:00:49,910
        }else {
            timepattern = Pattern.compile("\\d:\\d\\d:\\d\\d\\.\\d\\d,\\d:\\d\\d:\\d\\d\\.\\d\\d");
        }

        if(additionCondition){
            System.out.print("Amount in tenths of seconds: ");
            addConst = Integer.parseInt(input.nextLine().trim());

            System.out.print("Should this change be limited by a start time (y/n): ");
            conditionChar = input.nextLine().trim().charAt(0);
            final boolean startTimeCondition=(conditionChar=='y');

            if(startTimeCondition){
                System.out.print("Enter start time (h:m:s) each number with 2 digits: ");
                if (fileType) {
                    startTime = timeToNumSrt(input.nextLine().trim());
                }else{
                    startTime = timeToNumAss(input.nextLine().trim());
                }
            }
        }

        System.out.print("Apply proportional adjustment? (y/n): ");
        conditionChar = input.nextLine().trim().charAt(0);
        final boolean conversionCondition=(conditionChar=='y');

        int[] conversionFactors = new int[2];

        if(conversionCondition){
			System.out.println("Enter currentTime>correctTime for the first instance in tenths of seconds: ");
			String[] timingInstance = input.nextLine().trim().split(">");
                    int gt1 = Integer.parseInt(timingInstance[0]);
                    int ct1 = Integer.parseInt(timingInstance[1]);
					
			System.out.println("Enter currentTime>correctTime for the last instance in tenths of seconds: ");
			timingInstance = input.nextLine().trim().split(">");
                    int gt2 = Integer.parseInt(timingInstance[0]);
                    int ct2 = Integer.parseInt(timingInstance[1]);
					
			addConst = Math.round(ct1*gt2-ct2*gt1)/(ct2-ct1);
			additionCondition = !(addConst==0);
			
			conversionFactors[0] = ct2;
			conversionFactors[1] = gt2+addConst;
			/*
            System.out.println("Factor: \n1: 23.976->24.00 \n2: 23.940->24.00 \n3: enter manually");
            conditionChar = input.nextLine().trim().charAt(0);

            switch(conditionChar){
                case '1': conversionFactors[0]=23976;
                    conversionFactors[1]=24000;
                    break;
                case '2': conversionFactors[0]=23940;
                    conversionFactors[1]=24000;
                    break;
                case '3': System.out.println("enter numerator/denominator");
                    //String conversionString = input.nextLine().trim();
                    String[] conversionString = input.nextLine().trim().split("/");
                    conversionFactors[0]=Integer.parseInt(conversionString[0]);
                    conversionFactors[1]=Integer.parseInt(conversionString[1]);
                    break;
                default: conversionFactors[0]=1;
                    conversionFactors[1]=1;
                    break;
            }*/
        }

        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new FileReader(inputFileName));
            out = new PrintWriter(new FileWriter(outputFileName));
            int c;

            String l;
            String stime;
            /*runthrough of the input stream and extraction of the right lines*/
            while ((l = in.readLine()) != null) {

                Matcher m = timepattern.matcher(l);

                if (m.find()){

                    out.print(l.substring(0,m.start()));
                    stime = m.group();

                    /*transformation of times from string to int*/
                    long[] nums;
                    if (fileType){
                        nums = stringToNumsSrt(stime);
                    }else{
                        nums = stringToNumsAss(stime);
                    }


                    /*application of the previously chosen operations*/
                    if(nums[0]!=0){
                        if(additionCondition&&(nums[0]>startTime)){
                            if (fileType) {
                                nums[0]+=(addConst*100);
                                nums[1]+=(addConst*100);
                            }else{
                                nums[0]+=(addConst*10);
                                nums[1]+=(addConst*10);
                            }

                        }
                        if(conversionCondition){
                            nums[0]*=conversionFactors[0];
                            nums[0]/=conversionFactors[1];

                            nums[1]*=conversionFactors[0];
                            nums[1]/=conversionFactors[1];
                        }

                        /*transformation of int back into string*/
                        if (fileType) {
                            stime = numsToStringSrt(nums[0], nums[1]);
                        }else{
                            stime = numsToStringAss(nums[0], nums[1]);
                        }
                    }


                    out.print(stime);

                    out.println(l.substring(m.end()));

                }else{
                    out.println(l);
                }



            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}