package com.errplane.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPPostHelper {
	
	private HttpsURLConnection connection;
	
	public boolean sendPost(int numBytes) {
		
		if (connection == null) {
			return false;
		}
		if (numBytes <= 0) {
			// better close the connection
			try {
				connection.getOutputStream().close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				connection.disconnect();
			}
			return false;
		}
		//connection.setRequestProperty("Content-Length", "" + Integer.toString(numBytes));
		
		try {
			connection.getOutputStream().flush();
			if (connection.getResponseCode() != 201) {
				return false;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				connection.getOutputStream().close();
			}
			catch (Exception ex) {
				
			}
			finally {
				connection.disconnect();
			}
		}
		
		return false;
	}
	
	public OutputStream getOutputStream(URL epUrl) {
		OutputStream retStream = null;
		try {
			connection = (HttpsURLConnection) epUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setUseCaches (false);
			retStream = connection.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retStream;
	}
}
