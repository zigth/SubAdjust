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

    /*functions for transforming integers to srt or ass style strings*/
    public static String numsToString(long time1, long time2, String fileExt){
		String result;
		
        switch (fileExt) {
            case ".srt":
				result = numToString(time1, false, true, ',','2')+" --> "+numToString(time2, false, true, ',', '2');
				break;
				
            case ".ass":
				result = numToString(time1, true, false, '.','1')+","+numToString(time2, true, false, '.', '1');
				break;
				
            default:
				result = "";
        }		
		return result;
    }
	
	public static String numToString(long time, boolean useCenti, boolean useMilli, char decimalDivider, char leadingZeros){
		long milliseconds;
		long centiseconds;
		long seconds;
		long minutes;
		long hours;
		String shours;
		String sminutes;
		String sseconds;
		String scentiseconds;
		String smilliseconds;
		String result;
		
		milliseconds = time%1000;
		time = Math.round(time/10);
		centiseconds = time%100;
		time -= centiseconds;
        seconds = time%6000;
		time -= seconds;
		seconds /= 100;
		minutes = time%360000;
		time -= minutes;
		minutes /= 6000;
		hours = time/360000;

		shours = String.format("%0"+leadingZeros+"d",hours);
		sminutes = String.format("%02d",minutes);
		sseconds = String.format("%02d",seconds);
		scentiseconds = String.format("%02d",centiseconds);
		smilliseconds = String.format("%03d",milliseconds);
		
		result = shours+":"+sminutes+":"+sseconds;
		if (useCenti){
			result = shours+":"+sminutes+":"+sseconds+decimalDivider+scentiseconds;
		}
		if (useMilli){
			result = shours+":"+sminutes+":"+sseconds+decimalDivider+smilliseconds;
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
        /*Initialization of variables*/
        Scanner input = new Scanner(System.in);
        
		boolean fileNotFound = true;
		String[] fileTypes = {".srt", ".ass"};
        String fileType = "";
		String inputFileName = "";
		String outputFileName = "";
		String inputString = "";
		Pattern extensionPattern = Pattern.compile("\\.\\w+\\z");
		Path path;
		char conditionChar;
		boolean additionCondition = true;
		Pattern timepattern = Pattern.compile("");
		long startTime = 0;
		long addConst = 0;
		boolean conversionCondition = false;
		long[] conversionFactors = new long[2];
		long gt1 = 0;
		long ct1 = 0;
		long gt2 = 0;
		long ct2 = 0;
		
		int step = 1;
		boolean setup = true;
		/*loop for setting up the variables*/
		while (setup){
			switch (step){
				/*initialisation of input+output stream*/
				case 1:
					fileNotFound = true;
					inputFileName = "";
					fileType = "";
					System.out.print("Name of the subtitle file: ");
					inputFileName = input.nextLine().trim();
					
					Matcher m = extensionPattern.matcher(inputFileName);
					if(m.find()) {
						fileType = m.group();
					}
	
					if (Arrays.asList(fileTypes).contains(fileType)){
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
								inputFileName = testName;
							}
						}	
					}
					if (fileNotFound){
						System.out.print("File not found, would you like to try again? (y/n): ");
						conditionChar = input.nextLine().trim().toLowerCase().charAt(0);
						if(conditionChar!='y'){
							System.exit(0);
						}
					}
					if (!fileNotFound){
						outputFileName = inputFileName.substring(0, inputFileName.length() - fileType.length()).concat("2").concat(fileType);
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
						step = 2;
					}
					break;
				/*determination of the operations to be used*/
				case 2:	
					System.out.print("Apply linear adjustment? (y/n): ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						step = 1;
						break;
					}
					conditionChar = inputString.charAt(0);
					additionCondition=(conditionChar=='y');
					if (additionCondition){
						step = 3;
					}else{
						step = 6;
					}
					break;
				/*setting up the linear time adjustment*/
				case 3:	
					System.out.print("Amount in hundreths of seconds: ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						step = 2;
						break;
					}
					try{
						addConst = Integer.parseInt(inputString)*10;
						step = 4;
					}
					catch (NumberFormatException e)  {System.out.println("This is not a valid integer, try again."); } 	
					break;
				case 4:
					System.out.print("Should this change be limited by a start time (y/n): ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						step = 3;
						break;
					}
					conditionChar = inputString.charAt(0);
					if (conditionChar=='y'){
						step = 5;
					}else{
						step = 9;
					}
					break;
				case 5:
					startTime = 0;
					System.out.print("Enter start time (h:m:s): ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						step = 4;
						break;
					}
					startTime = timeToNum(inputString);
					if(startTime==-1){
						System.out.println("This is not a correct format, try again.");
					}else{
						step = 9;
					}
					break;
				/*setting up proportional time adjustment*/
				case 6:
					System.out.print("Apply proportional adjustment? (y/n): ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						step = 2;
						break;
					}
					conditionChar = inputString.charAt(0);
					conversionCondition=(conditionChar=='y');
					if (conversionCondition){
						step = 7;
					}else{
						step = 9;
					}
					break;
				case 7:
					String[] timingInstance;
					System.out.println("Enter currentTime>correctTime for the first instance in format (h:m:s:hundredthsOfSecond): ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						step = 6;
						break;
					}
					timingInstance = inputString.split(">");
					if (timingInstance.length>1){
						gt1 = timeToNumCenti(timingInstance[0]);
						ct1 = timeToNumCenti(timingInstance[1]);
						if ((gt1!=-1)&&(ct1!=-1)){
							step = 8;
						}
					}
					if(step==7){System.out.println("This is not a correct format, try again.");}
					break;					
				case 8:
					System.out.println("Enter currentTime>correctTime for the last instance in format (h:m:s:hundredthsOfSecond): ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						step = 7;
						break;
					}
					timingInstance = inputString.split(">");
					if (timingInstance.length>1){
						gt2 = timeToNumCenti(timingInstance[0]);
						ct2 = timeToNumCenti(timingInstance[1]);
						if ((gt2!=-1)&&(ct2!=-1)){
							step = 9;
						}
					}
					if(step==8){System.out.println("This is not a correct format, try again.");}
					if(step==9){		
						addConst = Math.round((ct1*gt2-ct2*gt1)/(ct2-ct1));
						additionCondition = !(addConst==0);
					
						conversionFactors[0] = ct2;
						conversionFactors[1] = gt2+addConst;}
					break;
				case 9:
					System.out.println("input file: "+inputFileName);
					if (additionCondition && !conversionCondition){
						System.out.println("adjustment type: linear");
						String sign = "";
						if (addConst < 0){sign="-";} 
						System.out.println("time offset: "+sign+Math.abs(addConst)/1000+"."+Math.round(Math.abs(addConst)/10)%100);
						if (startTime!=0){
							System.out.println("start time restriction: "+numToString(startTime,false,false,',','1'));
						}else{
							System.out.println("no start time restriction");
						}						
					}
					if (conversionCondition){
						System.out.println("First timing correction: "+numToString(gt1,true,false,',','1')+" > "+numToString(ct1,true,false,',','1'));
						System.out.println("Last timing correction: "+numToString(gt2,true,false,',','1')+" > "+numToString(ct2,true,false,',','1'));
					}
					if (!additionCondition && !conversionCondition){
						System.out.println("No changes made!");
					}
					System.out.println("Are these parameters correct? (y/back): ");
					inputString = input.nextLine().trim().toLowerCase();
					if (inputString.equals("back")){
						if (startTime!=0){
							step = 5;
							break;
						}
						if (conversionCondition){
							step = 8;
							break;
						}
						if (additionCondition){
							step = 4;
							break;
						}
						step = 6;
						break;
					}
					step = 10;
					break;
				/*setup of variables is now done*/	
				case 10:
					setup=false;
					if (!additionCondition && !conversionCondition){
						System.exit(1);
					}
					break;
				default:
					System.out.print("An error occured, exiting");
					System.exit(0);
					break;
			}
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