package com.errplane.util;

public class Base64 {
	static final String base64chars =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	public static String encode(String data) {
		if (data == null) {
			return null;
		}
	    String encStr = "";
	    String addPadStr = "";
	    
	    String paddedInput = data;
	    
	    // determine if we need to pad due to input not being multiple of 3
	    int padAmt = (3 - (data.length() % 3)) %3;
	    
	    for (int i = 0; i < padAmt; i++) {
	        addPadStr += "=";
	        paddedInput += "\0";
	    }
	    
	    // iterate in increments of 3 to produce the encoding
	    for (int i = 0; i < paddedInput.length(); i += 3) {
	        // turn each 3 into 1 24 bit #
	        int n = (paddedInput.charAt(i) << 16) +
	                (paddedInput.charAt(i+1) << 8) +
	                (paddedInput.charAt(i+2));
	                
	        // turn the 1 24 into 4 6 bit #s
	        int n1 = (n >> 18) & 63, n2 = (n >> 12) & 63, n3 = (n >> 6) & 63, n4 = n & 63;
	        
	        // use the four 6 bit'ers as indices into the base64chars string
	        encStr += base64chars.charAt(n1);
	        encStr += base64chars.charAt(n2);
	        encStr += base64chars.charAt(n3);
	        encStr += base64chars.charAt(n4);
	    }
	    
	    encStr = encStr.substring(0, encStr.length()-addPadStr.length());
	    encStr += addPadStr;
	        
	    return encStr;
	}
}
