package com.example.demostringsplit;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.net.HttpURLConnection;
import java.net.URL;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	/*	
	 String[] splts=	"5K-00.004\r\nW-00.004\n12.000 Kg\r12.000 gm\n12.000 Gms".split("[\\'W'\'5K'\'Kg'\'Gms'\'gms'\'Gm'\'gm'\'Kgs'\'KG'\\s\' '\'']");
		//5K00.004" & vbCrLf & "W-00.004" & vbCrLf & "W-00.004" & vbCrLf & "W-00.004" & vbCrLf & "W-00.004" & vbCrLf &		
		String wt="";
		for(int cnt=0;cnt<splts.length-1;cnt++)
		{
				if (splts[cnt].trim().matches(".*[0-9].*"))
				{
					wt=	splts[cnt].trim();
					if (Double.parseDouble(wt)>0) break;
				}
		}
		
		int dm=splts.length;
	*/
new updfile().execute();
	}

	private static Boolean upload(String sasUrl, String filePath, String mimeType)
	{ 
		try 
		{ // Get the file data 
			sasUrl="http://padhueds.blob.core.windows.net/easydemo/EazyStore.sqlite?sv=2014-02-14&sr=c&sig=aVhJ4O7K9%2B0161HRRF%2F2vAlggSaIs61cWV98BJ1DGXo%3D&st=2015-05-01T18%3A30%3A00Z&se=2015-05-09T18%3A30%3A00Z&sp=rw";
			filePath=Environment.getExternalStorageDirectory().getPath() + "/EazyStore";
			mimeType="application/octet-stream";
			
			File file = new File(filePath+"/EazyStore.sqlite"); 
			if (!file.exists()) { return false; } 
			String absoluteFilePath = file.getAbsolutePath(); 
			FileInputStream fis = new FileInputStream(absoluteFilePath);
			int bytesRead = 0; 
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			byte[] b = new byte[1024];
			while ((bytesRead = fis.read(b)) != -1)
			{ bos.write(b, 0, bytesRead); } 
			fis.close();
			byte[] bytes = bos.toByteArray();
			// Post our image data (byte array) to the server
			URL url = new URL(sasUrl.replace("\"", "")); 
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); 
			urlConnection.setDoOutput(true); 
			urlConnection.setConnectTimeout(15000); 
			urlConnection.setReadTimeout(15000);
			urlConnection.setRequestMethod("PUT"); 
			urlConnection.addRequestProperty("Content-Type", mimeType); //  text/html  ,  text/plain  , multipart/form-data
			urlConnection.setRequestProperty("Content-Length", "" + bytes.length);
			urlConnection.setRequestProperty("x-ms-blob-type", "BlockBlob"); 
			urlConnection.setRequestProperty("x-ms-blob-content-disposition","attachment;filename='EazyStore.sqlite'");
			// Write file data to server 
			DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream()); 
			wr.write(bytes); wr.flush();
			wr.close(); 
			int response = urlConnection.getResponseCode();
			if (response == 201 && urlConnection.getResponseMessage().equals("Created"))
			{ return true; } 
			} catch (Exception e)
			{ 
				 CommonData.WriteLog("DBSelectionActivity", "upload", e.toString());
			}
		return false; 
		}
	
	
	private static Boolean Download(String sasUrl, String filePath, String mimeType)
	{ 
		try 
		{ // Get the file data 
			sasUrl="http://padhueds.blob.core.windows.net/easydemo/EazyStore.sqlite";
			filePath=Environment.getExternalStorageDirectory().getPath() + "/EazyStore";
			mimeType="application/octet-stream";
			
			File file = new File(filePath+"/EazyStore_new.sqlite"); 
			String absoluteFilePath = file.getAbsolutePath(); 
			
			URL url = new URL(sasUrl.replace("\"", "")); 
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); 
			urlConnection.setDoInput(true); 
			urlConnection.setConnectTimeout(15000); 
			urlConnection.setReadTimeout(15000);
			urlConnection.setRequestMethod("GET"); 
			urlConnection.addRequestProperty("Content-Type", mimeType); //  text/html  ,  text/plain  , multipart/form-data
			urlConnection.setRequestProperty("x-ms-blob-type", "BlockBlob"); 
			// Write file data to server 
			DataInputStream wi = new DataInputStream(urlConnection.getInputStream());
			int lc=urlConnection.getContentLength();
			FileOutputStream fis = new FileOutputStream(absoluteFilePath);
			byte[] buffer=new byte[lc];
			wi.read(buffer);
			fis.write(buffer);
			fis.flush();			
			fis.close(); 
			wi.close();
			
			} catch (Exception e)
			{ 
				 CommonData.WriteLog("DBSelectionActivity", "upload", e.toString());
			}
		return false; 
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
class updfile extends AsyncTask<Void, integer, Void>
{

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		//upload("","","");
		Download("","","");
		return null;
	}
	
}

}
