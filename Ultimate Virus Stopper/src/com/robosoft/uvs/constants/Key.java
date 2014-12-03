package com.robosoft.uvs.constants;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.text.Text;

public class Key {

	/**
	 * the version of the operating system
	 */
	public static String OSversion = "";
        
        /**
         * for manipulating the dialog text from runtime
         */
        public static Text textInfo = null;
	
	/**
	 *status of real time protection 
	 */
	public static boolean RealTimeProtection = true;
	
        /**
         * no of threat deleted
         */
	public static int threatDeleted = 0;
        
        /**
         * get the current day 
         * @return 
         */
        public static String dateTime(){
            Calendar c=Calendar.getInstance();
            String val = "";
            
            val = ""+c.get(Calendar.YEAR);
            val += "/"+c.get(Calendar.MONTH);
            val += "/"+c.get(Calendar.DAY_OF_MONTH);
            
            val += " "+DateFormat.getInstance().format(new Date());
            
            return val;
        }
}
