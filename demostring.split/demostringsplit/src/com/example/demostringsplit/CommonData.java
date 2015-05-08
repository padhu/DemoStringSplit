package com.example.demostringsplit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import android.os.Environment;
import android.util.Log;


public class CommonData
{
	public static String BillStartingTime="5:00 AM";
	 public static String getExternalStorage()
	 {
		 String exts =  Environment.getExternalStorageDirectory().getPath();
	        try 
	        {
	           FileReader fr = new FileReader(new File("/proc/mounts"));       
	           BufferedReader br = new BufferedReader(fr);
	           String sdCard=null;
	           String line;
	           while((line = br.readLine())!=null)
	           {
	               if(line.contains("secure") || line.contains("asec")) continue;    

	               if((line.contains("fat") || line.contains("fuse")) &&  !line.contains("emulated")  && !line.contains("usb") && !line.contains("misc") && !line.contains("media_rw"))
	               {		            	   
		               String[] pars = line.split("\\s");
		               if(pars.length<2) continue;
		               if(pars[1].equals(exts)) continue;
		               sdCard =pars[1]; 
		               break;
	               }
	           }
	          fr.close();
	          br.close();
	          return sdCard;  

	    } catch (Exception e) 
	    {		       
	    	WriteLog("CommonData", "getExternalStorage", e.toString());
	    }
	   return null;
	}
	 
	 
	 public static void WriteLog(String Activity,String MethodName,String ErrorString)
	 {
		 	String SDCardPath;
			if(getExternalStorage()!=null)
			{
				SDCardPath=CommonData.getExternalStorage();
			}else
			{
				SDCardPath=Environment.getExternalStorageDirectory().getAbsolutePath();
			}	
			if (new File(SDCardPath + "/EazyStore/Log").exists()!=true)
			{
				new File(SDCardPath + "/EazyStore/Log").mkdir();
			}
			File file = new File(SDCardPath + "/EazyStore/Log/Log_"+ GetCurrentDate()+".txt");
			List<String> FileList=LoadDirectoryfiles(SDCardPath + "/EazyStore/Log",".txt");
			
		 try 
     	 {
			 if(FileList.size()>3)
				{
				 	File DeleteFile=new File(SDCardPath + "/EazyStore/Log/"+FileList.get(0).toString());
				 	if(DeleteFile.exists())
				 	{
				 		DeleteFile.delete();
				 	}
					FileList.remove(0);
				}
        	 if (!file.exists())
        	 {
        		 file.createNewFile();
        	 }
        	 FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
             BufferedWriter bw = new BufferedWriter(fw);
			 bw.write(android.text.format.DateFormat.format("hh:mm aa", Calendar.getInstance().getTime()).toString()+"==>"+Activity+"("+MethodName+")==>"+ErrorString+"\r\n");
			 bw.close();
			 Log.e(Activity+"("+MethodName+")",ErrorString);
			 fw=null;bw=null;bw=null;
		} catch (IOException e) 
		{
			Log.e("WriteLog", e.toString());
		}
		file=null;SDCardPath=null;
	 }
	
	 
	 public static List<String> LoadDirectoryfiles(String Path,String FileExtension) 
	    {
		 	List<String> item = new ArrayList<String>();
		 	 String[]    extension   = {FileExtension};
		 	List<String> itemsd = new ArrayList<String>();
	    	try
	    	{		    		
	    		File directory = new File(Path);
		    	if(directory.exists()) 
		        {
		    		File[] files = directory.listFiles();

		    		Arrays.sort( files, new Comparator()
		    		{
		    		    public int compare(Object o1, Object o2) 
		    		    {
		    		        if (((File)o1).lastModified() > ((File)o2).lastModified()) {
		    		            return -1;
		    		        } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
		    		            return +1;
		    		        } else {
		    		            return 0;
		    		        }
		    		    }

		    		}); 
			          for(int i=files.length-1; i>=0; i--) 
			          {		             
			        	  for (String anExt : extension)
			              {
			                  if (files[i].getName().endsWith(anExt))
			                  {  
			                	  item.add(files[i].getName());	
			                  }
			              }		        	  
			          }
		        }  
		    	
		    return item;
	    	}catch (Exception e)
	    	{	 WriteLog("CommonData", "LoadDirectoryfiles", e.toString());
	    		 item.add("Error:"+e.toString());
	    		 return item;
			}
	    }
	 
	 public static  String GetCurrentDate()
	 {
		String CurrentDate="",StartingTime="";

		Date cDate = null,sDate = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
		  
		try
		{				
			cDate=Calendar.getInstance().getTime();
			StartingTime=android.text.format.DateFormat.format("dd-MMM-yyyy", cDate.getTime()).toString()+" "+ CommonData.BillStartingTime;
	    	sDate = format.parse(StartingTime);		
	    	
	    	long diff = sDate.getTime() - cDate.getTime();
			if(diff>0)
			{
				cDate.setHours(cDate.getHours()-sDate.getHours());
				cDate.setMinutes(cDate.getMinutes()-sDate.getMinutes());
			}
			
			CurrentDate=android.text.format.DateFormat.format("dd-MMM-yyyy", cDate.getTime()).toString();	
		}catch (Exception e) 
		{
			WriteLog("CommonData", "GetCurrentDate", e.toString());
		}
		return CurrentDate;
	 }
	 
	 
}
