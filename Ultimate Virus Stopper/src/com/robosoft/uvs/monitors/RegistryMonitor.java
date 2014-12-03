package com.robosoft.uvs.monitors;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.robosoft.uvs.constants.Key;
import com.robosoft.uvs.registry.Registry;
import com.robosoft.uvs.registry.Registry.REGISTRY_ROOT_KEY;
import com.robosoft.uvs.registry.WinRegistry;
import java.io.File;

public class RegistryMonitor implements Runnable{
	private final String key = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			//add startup to it
                    String val = new File(new File("").getAbsolutePath()).getParent()+"\\Ultimate Virus Stopper.exe /onboot";
                    try {
                        WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, key, "Ultimate Virus Stopper", val, 0);
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                    }
                    
			while (Key.RealTimeProtection) {
                            System.out.println(Key.dateTime()+" Scanning Registry.......");
				try {
					TreeMap<String, Object> map = 
							Registry.getValues(REGISTRY_ROOT_KEY.CURRENT_USER, key);
					
					//get the list of all the values in this key
					Set<String> list = map.keySet();
					
					//search through list
					for (String value : list) {
						
						value = value.toLowerCase();
						//if virus script is found, delete
						if(containsIgnoreCase(""+map.get(value), ".vbs") || containsIgnoreCase(""+map.get(value), "cmd.exe")
                                                        || containsIgnoreCase(""+map.get(value), "googleupdate") 
                                                        || containsIgnoreCase(""+map.get(value), "AutoIt3")//characters confirmed
                                                        || containsIgnoreCase(""+map.get(value), "olfvir1")
                                                        || containsIgnoreCase(""+map.get(value), "olfvirl")
                                                        || containsIgnoreCase(""+map.get(value), "o1fvir1")
                                                        || containsIgnoreCase(""+map.get(value), "o1fvirl")){//not sure of characters spelling
							WinRegistry.deleteValue(WinRegistry.HKEY_CURRENT_USER, key, value, 0);
                                                        System.out.println(Key.dateTime()+" Virus Deleted from registry==> "+map.get(value));
						}
						
					}//end search
					
					
					Thread.sleep(60000*60);
					
					
				} catch (UnsupportedEncodingException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | InterruptedException e) {
				}
				
				
			}
			
		}
	}
	
	private boolean containsIgnoreCase( String haystack, String needle ) {
		  if(needle.equals(""))
		    return true;
		  
		  if(haystack == null || needle == null || haystack .equals(""))
		    return false; 

		  Pattern p = Pattern.compile(needle,Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
		  Matcher m = p.matcher(haystack);
		  return m.find();
	}
	
}