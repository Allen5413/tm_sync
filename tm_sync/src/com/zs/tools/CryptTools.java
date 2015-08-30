package com.zs.tools;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CryptTools {
	public static synchronized String Decrypt(String src) throws Exception{
		if(isValidString(src)){
			SecureRandom random = new SecureRandom();
			String pwd="eduwest1";
			DESKeySpec desKey = new DESKeySpec(pwd.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secureKey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, secureKey,random);
			byte[] result = cipher.doFinal(HexString2Bytes(src));
			return new String(result);
		}else{
			return "";
		}
	}
	
	private static String Bytes2HexString(byte[] b) { 
	    String ret = ""; 
	    for (int i = 0; i < b.length; i++) { 
	        String hex = Integer.toHexString(b[i] & 0xFF); 
	        if (hex.length() == 1) { 
	            hex = '0' + hex; 
	        } 
	        ret += hex.toUpperCase(); 
	    } 
	    return ret; 
	} 
	
	private static byte uniteBytes(byte src0, byte src1) { 
	    byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue(); 
	    _b0 = (byte)(_b0 << 4); 
	    byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue(); 
	    byte ret = (byte)(_b0 ^ _b1); 
	    return ret; 
	} 
	
	private static byte[] HexString2Bytes(String src){ 
	    byte[] ret = new byte[8]; 
	    byte[] tmp = src.getBytes(); 
	    for(int i=0; i<8; i++){ 
	     ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]); 
	    } 
	    return ret; 
	}
	
    public static synchronized String Encrypt(String datasource) {  
    	if(isValidString(datasource)){
        try{
        SecureRandom random = new SecureRandom();
        String pwd="eduwest1";
        DESKeySpec desKey = new DESKeySpec(pwd.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        byte[] result =  cipher.doFinal(datasource.getBytes());
        return Bytes2HexString(result);
        }catch(Throwable e){
                e.printStackTrace();
        }
        return null;
    	}
		else{
			return "";
		}
}
	public static boolean isValidString(Object obj){
		boolean isValid = false;
		if(null!=obj && obj.toString().trim().length()>0){
			isValid = true;
		}
		return isValid;
	}


	public static void main(String[] args) {
		String pwd = "888888";
		try {
			pwd = CryptTools.Encrypt(pwd);
			pwd = CryptTools.Decrypt(pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}
