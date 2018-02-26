package com.feicui.atm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import com.feicui.atm.entity.User;

/**
 * 工具类，里有公用方法
 * @author 宁强
 * 创建时间：2018-2-9 21:55:44
 */
public class Tool {	
	
	/**
	 * 静态方法接受用户输入
	 * @return 返回用户输入的
	 */
	public static String input () {
		
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String string = scanner.nextLine();
		//scanner.close();
		return string;
		
	}

	/**
	 * 从resources 路径总通过文件名找到文件并返回
	 * @param fileName	文件名
	 * @return 此文件
	 */
	private static File getFileFromRecourse(String fileName) {
		
		//找到此文件所在路径
		String filePath = Tool.class.getClassLoader().getResource(fileName).getFile();
		
		File file = new File(filePath);
		
		return file;
	}
	
	/**
	 * 字节流读取UI文件可以用static
	 * @param 文件名
	 * @return 返回读取出的UI界面
	 */
	public static void readUi(String fileName) {
		
		//找到文件
		File file = getFileFromRecourse(fileName);
		
		//输入流 文件到程序 
		try {
			
			FileInputStream fis = new FileInputStream(file);
			byte[] b = new byte[1024];
			int count = 0;
			while((count = fis.read(b)) != -1) {
				String str = new String(b,0,count);
				System.out.println(str);
			}
			
			//关闭流
			fis.close();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过传入的键 从properties文件中获取指定的字符串
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static String getValueFromProp(String key) throws IOException {
		
		Properties prop = new Properties();
		
		// ？？？
		prop.load(Tool.class.getClassLoader().getResourceAsStream("config.properties"));
		
		String value = prop.getProperty(key);
		
		return value;
	}
	
	/**
	 * @param className
	 * @return 根据类名获取的实例
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Object reflectInstanceFromClassName(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		Object instance = Class.forName(className).newInstance();
		return instance;
	}
	
	/**
	 * 根据key从config.properties中获取类名
	 * 根据类名获取实例
	 * @param key
	 * @return 返回key对应实例
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object reflectInstanceFromProp(String key) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		
		//获取根据指定键从prop文件获取类名
		String className = getValueFromProp(key);
		
		//获取实例并返回
		return reflectInstanceFromClassName(className);
	}
	
	/**
	 * 查看一个用户的信息
	 * @param userInfoMap 用户信息集合
	 * @param key	需要查看的用户的键
	 * @throws IOException 
	 */
	public static void look(HashMap<String, User> userInfoMap,String key) throws IOException {
		
	}
	
	/**
	 * 验证用户输入密码账号正确
	 * @return	错误返回“false”，正确返回该账号对应用户的键
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static boolean loginExit(HashMap<String, User> userInfoMap) throws IOException  {
		

			if (userInfoMap.isEmpty()) {
				
				System.out.println(Tool.getValueFromProp("M105"));
				
				//userInfoMap 为空证明没有查到数据
				return false;
			}
			
				return true;
	}
	
	/**
	 * 连接数据库的静态方法
	 * @return 返回创建的链接对象
	 */
	public static Connection connectMySql() {
		
		try {
			
			//注册驱动，创建连接
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/myatm","root","1301744815");
			
			//返回 con
			return con;
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
/*	*//**
	 * 从结果集合里获取出user对象
	 * @return user对象
	 * 适合结果集里只有一个对象
	 *//*
	public static  User getMapValue (HashMap<Integer, User> resultMap,User user) {
		
		Set<Integer> keys = resultMap.keySet();

		for (Integer integer : keys) {
			user.setId(resultMap.get(integer).getId());
			user.setUser_name(resultMap.get(integer).getUser_name());
			user.setUser_idNo(resultMap.get(integer).getUser_idNo());
			user.setGender(resultMap.get(integer).getGender());
			user.setBirthday(resultMap.get(integer).getBirthday());
			user.setAddress(resultMap.get(integer).getAddress());
			user.setRemark(resultMap.get(integer).getRemark());
			user.setBalance(resultMap.get(integer).getBalance());
			user.setCard(resultMap.get(integer).getCard());
			user.setUser_password(resultMap.get(integer).getUser_password());
			user.setStatus(resultMap.get(integer).getStatus());
		}
		
		return user;
		
	}*/
	
	/**
	 * 格式化输出当前时间
	 * @return 当前时间的格式化字符串
	 */
	public static String getDate() {

		
		//格式化输出时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(new Date());
	}
	
}
