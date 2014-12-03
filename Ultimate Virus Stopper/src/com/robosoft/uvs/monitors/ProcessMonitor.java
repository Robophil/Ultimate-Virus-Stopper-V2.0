package com.robosoft.uvs.monitors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.robosoft.uvs.constants.Key;

public class ProcessMonitor implements Runnable{
	
	private static final String[] win_7 = {"AutoIt3.exe","wscript.exe","wuauclt.exe","svchost.exe","olfvirl.exe","o1fvir1.exe","o1fvirl.exe"};
	
	private static final String[] win_8 = {"AutoIt3.exe","wscript.exe","wuauclt.exe","olfvir1.exe","olfvirl.exe","o1fvir1.exe","o1fvirl.exe"};
	
	private static String[] processes;
	
	
	public ProcessMonitor() {
		
	}
	
	
	private void setProcessMonitor(){
		//wait for OS to be detected
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
		}
        

            switch (Key.OSversion) {
                case "8":
                    //windows 8
                    processes =win_8;
                    break;
                case "7":
                    //windows 7
                    processes =win_7;
                    break;
                case "XP":
                    //windows XP
                    processes =win_8;
                    break;
                case "V":
                    // windows vista and vienne_
                    processes =win_7;
                    break;
            }
	}

	
	private void detectOs(){
		Runtime runtime = Runtime.getRuntime();
		String [] cmd = {"CMD","/C","wmic os get name <NUL"};
		String info = "";
		try {
			//read cmd1
			Process p1 = runtime.exec(cmd);
			BufferedReader in1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			String line1;
			while ((line1=in1.readLine()) != null) {
				info+=line1;
			}
			in1.close();
			
			
			info=info.replaceAll(" ", "");
			info=info.replaceAll("Name", "");
			String[] splitInf = info.split("Windows");
			info = splitInf[1];
			info = info.toUpperCase();
			
			if (info.startsWith("8")) {//windows 8
				Key.OSversion = "8";
                                System.out.println(Key.dateTime()+" Detected Os ==> Windows 8");
			} else if (info.startsWith("7")) {//windows 7
				Key.OSversion = "7";
                                System.out.println(Key.dateTime()+" Detected Os ==> Windows 7");
			} else if (info.startsWith("XP")) {//windows XP
				Key.OSversion = "XP";
                                System.out.println(Key.dateTime()+" Detected Os ==> Windows XP");
			} else if (info.startsWith("V")) {// windows vista and vienne_
				Key.OSversion = "V";
                                System.out.println(Key.dateTime()+" Detected Os ==> Windows Vista");
			}
			
			
		} catch (IOException e) {
		}
	}
	
	
	@Override
	public void run() {
		//detect the OS name
		detectOs();
		
		//set the processes to be monitored
		setProcessMonitor();
		
		while (true) {
			Runtime runtime = Runtime.getRuntime();
			
			while (Key.RealTimeProtection) {
                            System.out.println(Key.dateTime()+" Scanning all process");
				for (String process : processes) {
					String cmd="taskkill /F /IM "+process+ " <NUL";
					String excmd[]={"CMD","/C",cmd};
					try {
						runtime.exec(excmd);
					} catch (IOException e) {
					}
					
				}
				
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
				}
			}
			
		}
	}

}
