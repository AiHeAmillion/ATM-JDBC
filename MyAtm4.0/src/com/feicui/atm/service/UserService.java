package com.feicui.atm.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.feicui.atm.core.IService;
import com.feicui.atm.dao.Running_tabDao;
import com.feicui.atm.dao.UserDao;
import com.feicui.atm.entity.Running_tab;
import com.feicui.atm.entity.User;
import com.feicui.atm.utils.Tool;

/**
 * 用户业务类
 * @author 宁强
 * 创建时间：2018-2-19 19:53:00
 */
public class UserService implements IService{
	
	UserDao userDao = new UserDao();
	Running_tabDao running_tabDao = new Running_tabDao();
	
	/**
	 * 用户业务存钱
	 * @author 宁强
	 * 参数下标0为thisUser,1为存的钱数
	 */
	@Override
	public void saveMoney(Object...objects) throws IOException {
		
		//转换为user
		User user = (User)objects[0];
		
		//余额+存钱
		user.setBalance(user.getBalance()+(double)objects[1]);
		
		//更新数据库
		userDao.update(user, " balance ",user.getBalance(),user.getCard());
		
		
		try {
			
			//获取流水对象实例
			Running_tab running_tab = (Running_tab)Tool.reflectInstanceFromProp("Running_tab");
			
			//获取流水数据id最大值
			int id = running_tabDao.maxIdQuery(running_tab);
			
			//添加流水
			running_tabDao.save(running_tab, id+1,user.getCard(),user.getCard(),1,Tool.getDate(),"+"+(double)objects[1],user.getBalance());
		
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			
			e.printStackTrace();
		} 
		
	}

	/**
	 * 用户业取钱
	 * @author 宁强
	 * 参数下标0为thisUser,1为取的钱数
	 */
	@Override
	public void drawMoney(Object...objects) throws IOException {
		
		// 转换为user
		User user = (User)objects[0];

		//余额不足
		if (user.getBalance()<(double)objects[1]) {
			System.out.println(Tool.getValueFromProp("D002"));
		}
		
		// 余额+存钱
		user.setBalance(user.getBalance() - (double)objects[1]);

		// 更新数据库
		userDao.update(user, " balance ", user.getBalance(), user.getCard());
		
		try {

			// 获取流水对象实例
			Running_tab running_tab = (Running_tab) Tool.reflectInstanceFromProp("Running_tab");

			// 获取流水数据id最大值
			int id = running_tabDao.maxIdQuery(running_tab);

			// 添加流水
			running_tabDao.save(running_tab, id + 1, user.getCard(), user.getCard(), 2, Tool.getDate(), "-" + (double)objects[1],
					user.getBalance());

		} catch (ReflectiveOperationException e) {

			e.printStackTrace();
		} 
	}
	
	/**
	 * 用户业转账
	 * @author 宁强
	 * 参数下标0为thisUser,1被转账用户，2为转账钱数
	 */
	@Override
	public int transferMoney(Object...objects) throws IOException {
		
		// 转换为user
		User user = (User)objects[0];

		try {
			
			//查询转账账户是否存在 和是否为正常用户
			HashMap<Integer, Object> resultMap = userDao.query(user, Tool.getValueFromProp("TransferMoneySql"),objects[1],1);
			
			
			if (resultMap.isEmpty()) {
				
				//转账账户不存在
				return 1;
			}else if (user.getBalance() < (double)objects[2]) {
				
				//余额不足
				return 2;
			}
			
			//反射创建一个user2
			User user2 = (User)Tool.reflectInstanceFromProp("User");
			
			Set<Integer> keys = resultMap.keySet();
			for (Integer integer : keys) {
				user2 = (User)resultMap.get(integer);
			}
			
			//更新数据库
			userDao.update(user, " balance ", user.getBalance()-(double)objects[2], user.getCard());
			userDao.update(user, " balance ", user2.getBalance()+(double)objects[2], user2.getCard());
			
			// 获取流水对象实例
			Running_tab running_tab = (Running_tab) Tool.reflectInstanceFromProp("Running_tab");

			// 获取流水数据id最大值
			int id = running_tabDao.maxIdQuery(running_tab);

			// 添加流水 源账户
			running_tabDao.save(running_tab, id + 1, user.getCard(), user2.getCard(), 3, Tool.getDate(), "-" + (double)objects[2],
					user.getBalance() - (double)objects[2]);
			
			// 添加流水目标账户
			running_tabDao.save(running_tab, id + 2, user2.getCard(), user.getCard(), 4, Tool.getDate(), "+" + (double)objects[2],
					user2.getBalance() + (double)objects[2]);
			
			
		} catch (ReflectiveOperationException e) {
			
			e.printStackTrace();
		}
		return 0; 
		
	}
	
	//账户锁定的业务
	public void lockAccount(String card) throws IOException {
		
		// 锁定更新数据
		try {
			userDao.update((User)Tool.reflectInstanceFromProp("User"), " status ", 3, card);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean login(String card, String user_password) throws IOException {
		
		return false;
	
	}
	
	@Override
	public void openAccount(Object...objects) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clossAccount(Object...objects) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void unlockAccount(Object...objects) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetPassword(Object...objects) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public HashMap<Integer, Object> queryOne(Object...objects) throws IOException {
		return null;
		
	}
	/**
	 * 查询此用户流水
	 * @param user 需要查询的用户
	 * @return 这个用户的流水集合
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public HashMap<Integer, Object> lookRunning_tab(User user) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		
		//创建流水对象实例
		Running_tab running_tab = (Running_tab)Tool.reflectInstanceFromProp("Running_tab");
		
		//查询数据库
		return  running_tabDao.query(running_tab, Tool.getValueFromProp("Running_tabQuerySql"),user.getCard());
		
	}

	/**
	 * 通过已知账户查询数据库是否有这个对象
	 * @param card
	 * @param user
	 * @return 查到返回查出的这个对象   如果没有查到返回的是传入的对象
	 * @throws IOException
	 */
	public User query(String card,User user) throws IOException {
		
		HashMap<Integer, Object> resultMap = new HashMap<Integer, Object>();

		// 查询数据库
		try {
			
			resultMap = userDao.query(user, Tool.getValueFromProp("LoginSql"), card,card);
		
		} catch (ReflectiveOperationException e) {
			
			e.printStackTrace();
		}
		
		if (resultMap.isEmpty()) {
			
			//没有查到信息
			return user;
		}
		
		Set<Integer> keys = resultMap.keySet();

		for (Integer integer : keys) {
			
			user = (User)resultMap.get(integer);
	
		}
		
		return user;
		
	}
	
}
