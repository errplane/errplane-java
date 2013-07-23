package com.errplane.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPPostHelper {

	private HttpsURLConnection connection;

	public boolean sendPost(int numBytes) {
	  boolean returnValue = true;

	  try {
  		if (connection == null) {
  			return false;
  		}
  		if (numBytes > 0) {
  			connection.getOutputStream().flush();
  			if (connection.getResponseCode() != 201) {
  			  // System.out.printf("Got %d status code\n", connection.getResponseCode());
  				return false;
  			}
  		}
	  }
		catch (IOException e) {
		  returnValue = false;
			e.printStackTrace();
		}
		finally {
			closeConnection();
		}

		return returnValue;
	}

	private boolean closeConnection() {
    // better close the connection
    try {
      connection.getOutputStream().close();
    } catch (Exception e) {
      // e.printStackTrace();
    } finally {
      connection.disconnect();
    }
    return false;
	}

	public OutputStream getOutputStream(URL epUrl, int contentLength) {
		OutputStream retStream = null;
		try {
			connection = (HttpsURLConnection) epUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
			connection.setUseCaches (false);
			retStream = connection.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retStream;
	}
}
