import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.regex.*;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//import java.lang.*;
/*function for transforming srt or ass style strings into integers*/
public class SubAdjust {
    public static long[] stringToNums(String times, String fileExt){
        long[] result = new long[2];
		int hours;
		int minutes;
		int seconds;
		int milliseconds;
		int centiseconds;
		switch (fileExt) {
            case ".srt":
                hours = Integer.parseInt(times.substring(0,2));
				minutes = Integer.parseInt(times.substring(3,5));
				seconds = Integer.parseInt(times.substring(6,8));
				milliseconds = Integer.parseInt(times.substring(9,12));

				result[0] = hours*3600000+minutes*60000+seconds*1000+milliseconds;

				hours = Integer.parseInt(times.substring(17,19));
				minutes = Integer.parseInt(times.substring(20,22));
				seconds = Integer.parseInt(times.substring(23,25));
				milliseconds = Integer.parseInt(times.substring(26,29));

				result[1] = hours*3600000+minutes*60000+seconds*1000+milliseconds;
                break;
				
            case ".ass":
				hours = Integer.parseInt(times.substring(0,1));
				minutes = Integer.parseInt(times.substring(2,4));
				seconds = Integer.parseInt(times.substring(5,7));
				centiseconds = Integer.parseInt(times.substring(8,10));

				result[0] = hours*3600000+minutes*60000+seconds*1000+centiseconds*10;

				hours = Integer.parseInt(times.substring(11,12));
				minutes = Integer.parseInt(times.substring(13,15));
				seconds = Integer.parseInt(times.substring(16,18));
				centiseconds = Integer.parseInt(times.substring(19,21));

				result[1] = hours*3600000+minutes*60000+seconds*1000+centiseconds*10;
                break;
				
            default:
				result[0] = 0;
				result[1] = 0;
        }		
		return result;
    }

    /*function for transforming integers to srt or ass style strings*/
    public static String numsToString(long time1, long time2, String fileExt){
		long milliseconds1;
		long milliseconds2;
		long centiseconds1;
		long centiseconds2;
		long seconds1;
		long seconds2;
		long minutes1;
		long minutes2;
		long hours1;
		long hours2;
		String shours1;
		String shours2;
		String sminutes1;
		String sminutes2;
		String sseconds1;
		String sseconds2;
		String smilliseconds1;
		String smilliseconds2;
		String scentiseconds1;
		String scentiseconds2;
		String result;
		
        switch (fileExt) {
            case ".srt":
				milliseconds1 = time1%1000;
				time1 -= milliseconds1;
				seconds1 = time1%60000;
				time1 -= seconds1;
				seconds1 /= 1000;
				minutes1 = time1%3600000;
				time1 -= minutes1;
				minutes1 /= 60000;
				hours1 = time1/3600000;

				milliseconds2 = time2%1000;
				time2 -= milliseconds2;
				seconds2 = time2%60000;
				time2 -= seconds2;
				seconds2 /= 1000;
				minutes2 = time2%3600000;
				time2 -= minutes2;
				minutes2 /= 60000;
				hours2 = time2/3600000;


				shours1 = String.format("%02d",hours1);
				sminutes1 = String.format("%02d",minutes1);
				sseconds1 = String.format("%02d",seconds1);
				smilliseconds1 = String.format("%03d",milliseconds1);

				shours2 = String.format("%02d",hours2);
				sminutes2 = String.format("%02d",minutes2);
				sseconds2 = String.format("%02d",seconds2);
				smilliseconds2 = String.format("%03d",milliseconds2);

				result = shours1+":"+sminutes1+":"+sseconds1+","+smilliseconds1+" --> "+shours2+":"+sminutes2+":"+sseconds2+","+smilliseconds2;
				break;
				
            case ".ass":
				time1 = Math.round(time1/10);
				centiseconds1 = time1%100;
				time1 -= centiseconds1;
				seconds1 = time1%6000;
				time1 -= seconds1;
				seconds1 /= 100;
				minutes1 = time1%360000;
				time1 -= minutes1;
				minutes1 /= 6000;
				hours1 = time1/360000;

				time2 = Math.round(time2/10);
				centiseconds2 = time2%100;
				time2 -= centiseconds2;
				seconds2 = time2%6000;
				time2 -= seconds2;
				seconds2 /= 100;
				minutes2 = time2%360000;
				time2 -= minutes2;
				minutes2 /= 6000;
				hours2 = time2/360000;
				
				shours1 = String.format("%01d",hours1);
				sminutes1 = String.format("%02d",minutes1);
				sseconds1 = String.format("%02d",seconds1);
				scentiseconds1 = String.format("%02d",centiseconds1);

				shours2 = String.format("%01d",hours2);
				sminutes2 = String.format("%02d",minutes2);
				sseconds2 = String.format("%02d",seconds2);
				scentiseconds2 = String.format("%02d",centiseconds2);

				result = shours1+":"+sminutes1+":"+sseconds1+"."+scentiseconds1+","+shours2+":"+sminutes2+":"+sseconds2+"."+scentiseconds2;
				break;
				
            default:
				result = "";
        }		
		return result;
    }

	
	 /*function for transforming strings into integers*/
    public static long timeToNum(String time){
		Pattern numberPattern = Pattern.compile("\\d+");
		Matcher m = numberPattern.matcher(time);
        if(!m.find()){return -1;}
		int hours=Integer.parseInt(m.group());
        if(!m.find()){return -1;}
		int minutes=Integer.parseInt(m.group());
        if(!m.find()){return -1;}
		int seconds=Integer.parseInt(m.group());
		long result=hours*3600000+minutes*60000+seconds*1000;
        return result;
    }
	
	public static long timeToNumCenti(String time){
		Pattern numberPattern = Pattern.compile("\\d+");
		Matcher m = numberPattern.matcher(time);
		
		if(!m.find()){return -1;}
		int hours=Integer.parseInt(m.group());
        if(!m.find()){return -1;}
		int minutes=Integer.parseInt(m.group());
        if(!m.find()){return -1;}
		int seconds=Integer.parseInt(m.group());
		if(!m.find()){return -1;}
		int hundredths=Integer.parseInt(m.group());
      	
        long result=hours*3600000+minutes*60000+seconds*1000+hundredths*10;
        return result;
    }



    public static void main(String[] args) throws IOException {
        /*initialisation of input+output stream*/
        Scanner input = new Scanner(System.in);
        
        boolean invalidFileExtension = true;
		boolean fileNotFound = true;
		String[] fileTypes = {".srt", ".ass"};
        String fileType = "";
		String inputFileName = "";
		Pattern extensionPattern = Pattern.compile("\\.\\w+\\z");
		Path path;
		
        while (invalidFileExtension || fileNotFound) {
			System.out.print("Name of the subtitle file: ");
			inputFileName = input.nextLine().trim();
			
			Matcher m = extensionPattern.matcher(inputFileName);
			if(m.find()) {
				fileType = m.group();
			}

			if (Arrays.asList(fileTypes).contains(fileType)){
				invalidFileExtension=false;
				path = Paths.get(inputFileName);
				if (Files.exists(path)){fileNotFound = false;}
			}else{
				int i;
				String testName;
				for (i = 0; i < fileTypes.length; i++) {
					testName = inputFileName.concat(fileTypes[i]);
					path = Paths.get(testName);
					if (Files.exists(path)){
						fileNotFound = false;
						fileType = fileTypes[i];
						invalidFileExtension = false;
						inputFileName=testName;
					}
				}
			}
			if (invalidFileExtension || fileNotFound){
				System.out.print("File not found, would you like to try again? (y/n): ");
				char conditionChar = input.nextLine().trim().charAt(0);
				if(conditionChar!='y'){
					System.exit(0);
				}
			} 

        }
				
        String outputFileName = inputFileName.substring(0, inputFileName.length() - fileType.length()).concat("2").concat(fileType);
        		
        /*determination of the operations to be used*/
		System.out.print("Apply linear adjustment? (y/n): ");
        char conditionChar = input.nextLine().trim().charAt(0);
        boolean additionCondition=(conditionChar=='y');

        long startTime=0;
        long addConst=0;
        Pattern timepattern;
		switch (fileType) {
            case ".srt":
                timepattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d[,.]\\d\\d\\d.....\\d\\d:\\d\\d:\\d\\d[,.]\\d\\d\\d");
				//00:00:47,840 --> 00:00:49,910
                break;
            case ".ass":
                timepattern = Pattern.compile("\\d:\\d\\d:\\d\\d\\.\\d\\d,\\d:\\d\\d:\\d\\d\\.\\d\\d");
                break;
            default:
				timepattern = Pattern.compile("");
                System.out.print("An error occured, exiting");
				System.exit(0);
        }
		boolean retry=false;
        if(additionCondition){
			retry=true;
			while (retry){
				System.out.print("Amount in hundreths of seconds: ");
				try{
					addConst = Integer.parseInt(input.nextLine().trim())*10;
					retry=false;
				}
				catch (NumberFormatException e)  {System.out.println("This is not a valid integer, try again."); } 				
			}
            System.out.print("Should this change be limited by a start time (y/n): ");
            conditionChar = input.nextLine().trim().charAt(0);
            final boolean startTimeCondition=(conditionChar=='y');

			retry=true;
            while(startTimeCondition && retry){
                System.out.print("Enter start time (h:m:s): ");
                startTime = timeToNum(input.nextLine().trim());
				if(startTime==-1){
					System.out.println("This is not a correct format, try again.");
				}else{
					retry=false;
				}
            }
        }

		boolean conversionCondition=false;
		if(!additionCondition){
			System.out.print("Apply proportional adjustment? (y/n): ");
			conditionChar = input.nextLine().trim().charAt(0);
			conversionCondition=(conditionChar=='y');
		}
		
        long[] conversionFactors = new long[2];

        if(conversionCondition){
			long gt1=0;
			long ct1=0;
			long gt2=0;
			long ct2=0;
			String[] timingInstance;
			retry=true;
			while(retry){
				System.out.println("Enter currentTime>correctTime for the first instance in format (h:m:s:hundredthsOfSecond): ");
				timingInstance = input.nextLine().trim().split(">");
				if (timingInstance.length>1){
					gt1 = timeToNumCenti(timingInstance[0]);
					ct1 = timeToNumCenti(timingInstance[1]);
					if ((gt1!=-1)&&(ct1!=-1)){retry=false;}
				}
				if(retry){System.out.println("This is not a correct format, try again.");}
			}
			retry=true;
			while(retry){
				System.out.println("Enter currentTime>correctTime for the last instance in format (h:m:s:hundredthsOfSecond): ");
				timingInstance = input.nextLine().trim().split(">");
                if (timingInstance.length>1){
					gt2 = timeToNumCenti(timingInstance[0]);
                    ct2 = timeToNumCenti(timingInstance[1]);
					if ((gt2!=-1)&&(ct2!=-1)){retry=false;}
				}
				if(retry){System.out.println("This is not a correct format, try again.");}
			}		
			addConst = Math.round((ct1*gt2-ct2*gt1)/(ct2-ct1));
			additionCondition = !(addConst==0);
			
			conversionFactors[0] = ct2;
			conversionFactors[1] = gt2+addConst;
        }

        BufferedReader in = null;
        PrintWriter out = null;

        try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName),"UTF8"));
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFileName),"UTF8"));
			int c;

            String l;
            String stime;
			boolean printCond = true;
            /*runthrough of the input stream and extraction of the right lines*/
            while ((l = in.readLine()) != null) {
				Matcher m = timepattern.matcher(l);

                if (m.find()){

                    out.print(l.substring(0,m.start()));
                    stime = m.group();

                    /*transformation of times from string to int*/
                    long[] nums;
                    nums = stringToNums(stime,fileType);
                    
                    /*application of the previously chosen operations*/
                    if(nums[0]!=0){
                        if(additionCondition&&(nums[0]>startTime)){
                           	nums[0]+=(addConst);
							nums[1]+=(addConst);
                        }
                        if(conversionCondition){
                            nums[0]*=conversionFactors[0];
                            nums[0]/=conversionFactors[1];

                            nums[1]*=conversionFactors[0];
                            nums[1]/=conversionFactors[1];
                        }

                        /*transformation of int back into string*/
                        stime = numsToString(nums[0], nums[1], fileType);
                    }

					printCond = (nums[0]>=0);
		
					if (printCond){
						out.print(stime);

						out.println(l.substring(m.end()));
					}
					
                }else{
					if(printCond){
						out.println(l);
					}
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