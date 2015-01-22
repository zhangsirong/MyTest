package com.zhang.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * md5加密方法
	 * @param password
	 * @return
	 */
	public static String md5PassWord(String password) {
		//得到一个信息摘要器
				try {
					MessageDigest digest = MessageDigest.getInstance("md5");
					byte[] result = digest.digest(password.getBytes());
					StringBuffer buffer = new StringBuffer();
					//把每一个byte做一个运算0xff
					for (byte b:result){
						int number = b & 0xff;
						String str = Integer.toHexString(number);
						//System.out.println(str);
						if(str.length()==1){
							buffer.append("0");
						}
						buffer.append(str);
					}
					//加密的结果
					return buffer.toString();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					return "";
				}
			}
	}
	