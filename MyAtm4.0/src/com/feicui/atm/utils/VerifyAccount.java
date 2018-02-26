package com.feicui.atm.utils;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.feicui.atm.entity.User;


public class VerifyAccount {
	
	/**
	 * 验证输入号的格式
	 * @param strNumber 用户输入的登陆号 账号/身份证号
	 * @return 输入的号格式正确返回true 错误返回false
	 */
	public static boolean numberLengeth(String strNumber) {
		
		/*switch (strNumber.length()) {
		
		//长度 21为账号
		case 21:
			
			//正则表达式  0-9数字组成 19位
			Pattern pattern21 = Pattern.compile("[0-9][A-Z]{20}");
			Matcher matcher21 = pattern21.matcher(strNumber);
			
			return matcher21.matches();
		
		//长度 18为身份证号
		case 18:
			
			
			//正则表达式验证身份证号格式
			Pattern pattern18 = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");//正则表达式身份证号
			Matcher matcher18 = pattern18.matcher(strNumber);
			
			return matcher18.matches();
			
		default:
			
			// 输入的长度既不是21又不是18直接返回false
			return false;
			
		
		}*/
		
		//正则表达式  0-9A-Z数字组成 18位
		Pattern pattern = Pattern.compile("[0-9A-Z]{18}");
		Matcher matcher = pattern.matcher(strNumber);
		return matcher.matches();
	}
	
	/**
	 * 共用可以static修饰
	 * 验证账号或者身份证号是否存在
	 * @param userInfoMap
	 * @param strNumber 输入的账号或者密码
	 * @return 存在返回输入账号对应对象在Map中的键，不存在返回key="false"
	 */
	public static String numberExist(HashMap<String, User> userInfoMap,String strNumber) {
		
		//获取Map键的Set集
		String key = "false";
		Set<String> keys = userInfoMap.keySet();
		
		boolean bln = false;
		switch (strNumber.length()) {
		case 21: //账号长度
			
			for(String i : keys) {
				bln = strNumber.equals(userInfoMap.get(i).getCard());
				
				//当bln 变为true 找到账号
				if (bln) {	
					key = i;//key为此对象在键值对的键
				}
			}
			return key;
			
		case 18: //身份证号长度
			
			for(String i : keys) {
				bln = strNumber.equals(userInfoMap.get(i).getUser_idNo());
				
				//当bln 变为true 找到身份证号
				if (bln) {	
					key = i;//key为此对象在键值对的键
				}
			}
			return key;
			
		default:
			return key;
		}
	}
	
	/**
	 * 验证密码的格式以及长度
	 * @param strPassword 传入参数为输入的密码
	 * @return 返回false密码格式错误 返回true密码格式正确
	 */
	public static boolean passwordLength(String strPassword) {
		
		//正则表达式 6位,数字和字母必须同时有,至少一个大写字母	
		return strPassword.matches("([A-Za-z0-9]{6})");
		
	}
	
	/**
	 * 共用可以static修饰
	 * 验证密码的正确性
	 * @param userInfoMap
	 * @param key
	 * @param strPassword
	 * @return 返回false 密码错误   返回true 密码正确 
	 */
	public static boolean passwordExist(HashMap<String, User> userInfoMap,String key,String strPassword) {
		
		//如果传入key="false" 直接返回false
		if ("false".equals(key)) {
			return false;
		}
		
		boolean temp = strPassword.equals(userInfoMap.get(key).getUser_password());
		return temp;	
	}
}
