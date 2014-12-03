/*
 * Copyright 2014 Balogun Oghenerobo Philip
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.robosoft.uvs.monitors;

import com.robosoft.uvs.constants.Key;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;

/**
 *
 * @author IamJosh
 */
public class scanDirectory implements Runnable{
    private File dir;
    
    public scanDirectory(final File dir){
        this.dir = dir;
    }

    @Override
    public void run() {
        final File[] files = dir.listFiles();//get list of all the files in the directory
        final ExecutorService ex = Executors.newCachedThreadPool();
        
        for (File file : files) {//for each file in this directory
            String filePath = file.getAbsolutePath();
            
            //if file is a directory, create a new thread to scan it
            if (file.isDirectory()) {
                ex.execute(new scanDirectory(file));
            } else {
                if(containsIgnoreCase(filePath, ".lnk") || containsIgnoreCase(filePath, ".vbs")
                  || containsIgnoreCase(filePath, "AutoIt3.exe")){
                        file.delete();
                        System.out.println(Key.dateTime()+" deleted "+file.getAbsolutePath());
                        Platform.runLater(()->{
                            Key.textInfo.setText((Key.threatDeleted+=1)+" threat deleted");
                        });
                }
            }
        }
        //shut down the thread creator
        ex.shutdown();
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
