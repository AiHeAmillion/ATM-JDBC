package com.feicui.atm.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.feicui.atm.core.IService;
import com.feicui.atm.dao.UserDao;
import com.feicui.atm.entity.User;
import com.feicui.atm.utils.Tool;
import com.feicui.atm.utils.VerifyAccount;

/**
 * 管理员业务类
 * @author 宁强
 * 创建时间：2018-2-19 19:52:39
 */
public class AdminService implements IService{
	
	UserDao userDao = new UserDao();
	
	/**
	 * 管理员账户密码验证查询数据库
	 * @author 宁强
	 * 错误返回false 正确返回true
	 */
	@Override
	public boolean login(String card, String user_password) throws IOException {

		try {
			
			//查询数据库
			HashMap<Integer, Object> resultMap = userDao.query(new User(), Tool.getValueFromProp("LoginQuerySql"),card,123456,user_password,0);
		
			return !resultMap.isEmpty();
				
		} catch (ReflectiveOperationException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	//开户
	@Override
	public void openAccount(Object...objects) throws IOException {
	
		// 写入数据库
		userDao.save(new User(), objects);
		
	}
	
	//销户
	@Override
	public void clossAccount(Object...objects) throws IOException {
	
		// 将该用户状态码改为 2 销户
		userDao.update(new User(), "status", 2, objects[0]);
	}
	
	//显示用户返回用户的集合
	@Override
	public HashMap<Integer, Object> queryOne(Object...objects) throws IOException {

		// 查询数据库
		HashMap<Integer, Object> resultMap;
		try {

			resultMap = userDao.query(new User(), Tool.getValueFromProp("StatusQuerySql"), objects[0]);

			return resultMap;
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {

			e.printStackTrace();
		}
		return null;

	}
	
	//解锁锁定用户
	@Override
	public void unlockAccount(Object...objects) throws IOException {

		// 解除锁定
		userDao.update(new User(), " status ",1, objects[0]);
		
	}
	
	//重置密码
	@Override
	public void resetPassword(Object...objects) throws IOException {
		
		// 重置密码
		userDao.update(new User(), "user_password ",objects[0],objects[1]);
	}
	
	/**
	 * 验证身份证正确性和唯一性
	 * @param user_idNo
	 * @return 正确唯一返回true 错误返回false
	 * @throws IOException
	 */
	public boolean inputUser_idNo(String user_idNo) throws IOException {

		// 验证长度格式是否正确
		boolean temp = VerifyAccount.numberLengeth(user_idNo);

		try {

			// 调用userDao查询方法查询身份证号唯一
			HashMap<Integer, Object> resultMap = userDao.query((User) Tool.reflectInstanceFromProp("User"),
					Tool.getValueFromProp("User_idNoQuerySql"), user_idNo);

			return temp && resultMap.isEmpty();

		} catch (ReflectiveOperationException e) {

			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * 检测输入性别是否为1/2
	 * @param gender
	 * @return 是返回true 不是返回false
	 */
	public boolean inputGender(int gender) {

			if (1 == gender) {
				return true;
			} else if (2 == gender) {
				return true;
			}
			
			return false;
	}
	

	// 生成账号
	public String inputCard(int gender,String user_idNo) {

		// 账号 BC18 + 性别(男01:女02)  + 出生日期 + 4位随机号
		//截取身份证号的出生日期 前闭后开
		String sb1 = user_idNo.substring(6, 14);
		
		//设置随机数
		Random random = new Random();
	
		String card = "BC180"+gender+sb1+random.nextInt(9)+""+random.nextInt(9)+""+random.nextInt(9)+""+random.nextInt(9);
		
		return card;
	}
	
	//生成ID
	public int inputID() {
		
		//查出数据库中最大ID
		int id = userDao.maxIdQuery(new User())+1;
		
		return id;
		
	}
	
	//接受用户输入转成格式化时间
	public Date getDate(String dateString) {
		
	
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");

		try {
			
			Date date = fmt.parse(dateString);
			return date;
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * 根据账号查询数据库是否有此账户
	 * @param card
	 * @return 有返回true 没有返回false
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean queryAccount(String card,int status) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		
		//查询数据库
		HashMap<Integer, Object> resultMap = userDao.query(new User(), Tool.getValueFromProp("TransferMoneySql"),card,status);
		
		return !resultMap.isEmpty();
		
	}
	
	//把输入的时间转成格式化字符串返回
	public String dateString (Date date) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
		return sdf.format(date);
	}

	@Override
	public void saveMoney(Object... objects) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawMoney(Object... objects) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int transferMoney(Object... objects) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}
