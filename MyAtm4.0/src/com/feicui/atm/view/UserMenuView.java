package com.feicui.atm.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.feicui.atm.core.AbstractView;
import com.feicui.atm.core.RequestMap;
import com.feicui.atm.entity.Running_tab;
import com.feicui.atm.entity.User;
import com.feicui.atm.service.UserService;
import com.feicui.atm.utils.Tool;
import com.feicui.atm.utils.VerifyAccount;

/**
 * 用户业务选择界面
 * @author 宁强
 * 创建时间：2018-2-9 22:04:12
 */
public class UserMenuView extends AbstractView{
	
	@Override
	public void view(RequestMap requestMap) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		//获取用户业务类实例
		UserService userService = (UserService)Tool.reflectInstanceFromProp("UserService");
		User user = (User) Tool.reflectInstanceFromProp("User");
		
		//调用登录
		user = loginView(userService,user);

		//如果用户的ID为0   或者  状态不等于 1   那么此用户不得登录
		if ( (0 == user.getId()) || !(1 == user.getStatus()) ) {

			// 账号密码登录没通过验证
			requestMap.setDisPath("LoginView");
			return;

		}
		
		while (true) {
			
			User thisUser = (User)userService.query(user.getCard(),user);
			
			//读取用户业务选择界面
			Tool.readUi("user_menu.txt");
			String input = Tool.input();
			
			if ("1".equals(input)) {
				saveMoneyView(userService, thisUser);
			}else if ("2".equals(input)) {
				drawMoneyView(userService, thisUser);
			}else if ("3".equals(input)) {
				transferMoneyView(userService, thisUser);
			}else if ("4".equals(input)) {
				queryView(userService, thisUser);
			}else if ("5".equals(input)) {
				requestMap.setDisPath("LoginView");
				break;
			}
		}
		
	}

	/**
	 * 用户登录验证账号密码，密码错误三次，锁定用户
	 * @param userService
	 * @param user
	 * @return 账户密码正确 返回此用户的user对象   错误返回false
	 * @throws IOException
	 */
	private User loginView(UserService userService,User user) throws IOException {
		
		int count = 1;
		
		// 读取账号密码输入
		Tool.readUi("card_password.txt");
		String card = Tool.input();
		String user_password = Tool.input();
		
		// 验证长度格式
		boolean bln = VerifyAccount.numberLengeth(card);

		// 账号格式错误
		if (!bln) {
			System.out.println(Tool.getValueFromProp("M107"));
			return user;
		}
		
		//查询数据库
		user = userService.query(card, user);
		
		
		//没有数据  返回的是传入的对象
		if (0 == user.getId()) {
			
			//账户不存在
			System.out.println(Tool.getValueFromProp("A008"));
			return user;
		}
		
		if ( !(1 ==  user.getStatus() ) ) {
			
			//用户状态不是正常用户
			System.out.println(Tool.getValueFromProp("M113"));
			return user;
		
		}
		
		//验证密码
		while ( !user_password.equals( user.getUser_password() ) ) {
			
			if (3 == count) {
				
				//密码错误与三次锁定
				System.out.println(Tool.getValueFromProp("A024"));
				
				//锁定用户
				userService.lockAccount(user.getCard());
				
				//更过用户状态为 3 锁户状态 在内存中
				user.setStatus(3);
				
				return user;
			}
			
			System.out.println(Tool.getValueFromProp("A022")+count+Tool.getValueFromProp("A023"));
			
			//接受用户下次输入密码
			user_password = Tool.input();
			count++;
			
		}

		return user;
		
	}
	
	private void saveMoneyView(UserService userService,User user) throws IOException {
		
		//界面显示
		System.out.println(Tool.getValueFromProp("S001"));
		System.out.println(Tool.getValueFromProp("S002"));
		
		//接受用户存钱数
		double money = Double.valueOf(Tool.input());
		
		//更新数据库
		userService.saveMoney(user,money);
		
		//操作成功
		System.out.println(Tool.getValueFromProp("M003"));
	
	}
	
	private void drawMoneyView(UserService userService,User user) throws IOException {
		
		//界面显示
		System.out.println(Tool.getValueFromProp("D001"));
		System.out.println(Tool.getValueFromProp("D003"));
		
		//接受用户取钱数
		double money = Double.valueOf(Tool.input());
		
		// 更新数据库
		userService.drawMoney(user, money);

		// 操作成功
		System.out.println(Tool.getValueFromProp("M004"));
	}
	
	private void transferMoneyView(UserService userService,User user) throws IOException {
		
		//显示界面
		System.out.println(Tool.getValueFromProp("Z001"));
		System.out.println(Tool.getValueFromProp("Z002"));
		System.out.println(Tool.getValueFromProp("Z003"));
		
		//接受用户输入
		String card = Tool.input();
		double money = Double.valueOf(Tool.input());
		
		// 更新数据库
		int result = userService.transferMoney(user,card, money);
		
		if (1 == result) {
			System.out.println(Tool.getValueFromProp("Z004"));
		}else if (2 == result) {
			System.out.println(Tool.getValueFromProp("Z005"));
		}else {
			
			// 操作成功
			System.out.println(Tool.getValueFromProp("M005"));
		}	
	}
	
	private void queryView(UserService userService,User user) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		//显示界面
		System.out.println(Tool.getValueFromProp("C001"));
		System.out.println(Tool.getValueFromProp("C002"));
		
		//接受用户输入
		String input = Tool.input();
		
		//查询数据库 余额可能会变化，需要再查数据库
		user = (User)userService.query(user.getCard(), user);

		if ("1".equals(input)) {

			// 查看个人信息
			System.out.println(user.getString());
			
		} else if ("2".equals(input)) {
			
			System.out.println(Tool.getValueFromProp("L201"));
			
			//创建流水对象实例
			Running_tab running_tab = (Running_tab)Tool.reflectInstanceFromProp("Running_tab");
			
			//查询数据库
			HashMap<Integer, Object> resultMap = userService.lookRunning_tab(user);
			
			Set<Integer> keys = resultMap.keySet();
			
			for (Integer integer : keys) {
				
				running_tab = (Running_tab)resultMap.get(integer);
				
				//显示流水
				System.out.println(running_tab.getString());
				
			}
			
		}
	}
}
