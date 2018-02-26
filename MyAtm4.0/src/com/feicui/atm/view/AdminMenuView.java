package com.feicui.atm.view;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.feicui.atm.core.AbstractView;
import com.feicui.atm.core.RequestMap;
import com.feicui.atm.entity.User;
import com.feicui.atm.service.AdminService;
import com.feicui.atm.utils.Tool;
import com.feicui.atm.utils.VerifyAccount;

/**
 * 管理员业务选择界面
 * @author 宁强
 * 创建时间：2018-2-9 22:03:50
 */
public  class AdminMenuView extends AbstractView {
	
	@Override
	public void view(RequestMap requestMap) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		//获取管理员业务类实例
		AdminService adminService = (AdminService)Tool.reflectInstanceFromProp("AdminService");
		
		//调用登录验证
		boolean bln = loginView(adminService);
		
		//账号密码错误
		if (!bln) {
			System.out.println(Tool.getValueFromProp("M111"));
			requestMap.setDisPath("LoginView");
			return;
		}
		
		while (true) {
			
			//读取管理员业务选择界面
			Tool.readUi("admin_menu.txt");		
			String input = Tool.input();
			
			if ("1".equals(input)) {
				openAccountView(adminService);
			}else if ("2".equals(input)) {
				closeAccountView(adminService);
			}else if ("3".equals(input)) {
				queryOneView(adminService);
			}else if ("4".equals(input)) {
				unlockAccountView(adminService);
			}else if ("5".equals(input)) {
				resetPasswordView(adminService);
			}else if ("6".equals(input)) {
				requestMap.setDisPath("LoginView");
				return;
			}
		}
		
	}
	
	private boolean loginView(AdminService adminService) throws IOException {
		
		// 读取账号密码输入
		Tool.readUi("card_password.txt");
		String card = Tool.input();
		String user_password = Tool.input();
		
		//查询数据库
		return adminService.login(card, user_password);
	}
	
	private void openAccountView(AdminService adminService) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		// 读取开户界面
		Tool.readUi("open_account.txt");

		// 姓名
		String user_name = Tool.input();
		
		// 身份证号
		String user_idNo;
		
		while (true) {
			
			// 身份证号
			user_idNo = Tool.input();
			
			boolean bln = adminService.inputUser_idNo(user_idNo);
			
			if (bln) {
				break;
			}
			
			System.out.println(Tool.getValueFromProp("M109"));
			
		}
		

		// 性别
		int gender;
		
		while (true) {

			// 身份证号
			gender = Integer.valueOf(Tool.input());

			boolean bln = adminService.inputGender(gender);

			if (bln) {
				break;	
			}
			
			System.out.println("M110");
			
		}

		// 出生日期
		Date birthday = adminService.getDate(Tool.input());

		// 地址
		String address = Tool.input();

		// 备注
		String remark = Tool.input();

		// 账号
		String card = adminService.inputCard(gender, user_idNo);
		System.out.println(Tool.getValueFromProp("A001") + card);

		// 输入密码
		System.out.println(Tool.getValueFromProp("A003"));
		
		String user_password;
		
		while (true) {
			
			user_password = Tool.input();
			
			//验证密码格式
			boolean bln = VerifyAccount.passwordLength(user_password);
			
			if (bln) {
				break;
			}
			
			System.out.println(Tool.getValueFromProp("A026"));
			
		}
		
		// 再次输入密码
		System.out.println(Tool.getValueFromProp("A004"));
		
		String user_password2 = Tool.input();

		if (!user_password.equals(user_password2)) {

			System.out.println(Tool.getValueFromProp("A005"));

		} else {

			System.out.println(Tool.getValueFromProp("A006"));
			
			// 生成id
			int id = adminService.inputID();
			
			System.out.println(Tool.getValueFromProp("A002") + id);
			
			//存入数据库
			adminService.openAccount(id, user_name, user_idNo, gender, adminService.dateString(birthday), address, remark, 0, card,
					user_password, 1);
		}
	
	}
	
	private void closeAccountView(AdminService adminService) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		String card;
		
		while (true) {
			
			System.out.println(Tool.getValueFromProp("A007"));

			// 接受用户输入
			 card= Tool.input();

			boolean bln = adminService.queryAccount(card,1);

			if (bln) {
				break;
			}
			
			// 没查到账号
			System.out.println(Tool.getValueFromProp("A008"));
			
		}
		
		//更新数据库销户
		adminService.clossAccount(card);
		System.out.println(Tool.getValueFromProp("M102"));
	}
	
	private void queryOneView(AdminService adminService) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		//显示界面
		Tool.readUi("admin_query.txt");
		
		//接受用户输入
		int temp = Integer.valueOf(Tool.input());
		
		//查询数据库
		HashMap<Integer, Object> resultMap = adminService.queryOne(temp);
		
		//没有查到用户信息
		if (resultMap.isEmpty()) {
			System.out.println(Tool.getValueFromProp("A016"));
			return;
		}
		
		User user = (User) Tool.reflectInstanceFromProp("User");
		
		System.out.println(Tool.getValueFromProp("A010") + "\n");

		// 获取map键的set集
		Set<Integer> keys = resultMap.keySet();

		// 键集遍历
		for (Integer i : keys) {

			user = (User) resultMap.get(i);

			// 通过键获取值，值是user对象
			System.out.println(Tool.getValueFromProp("A002") + user.getId());
			System.out.println(Tool.getValueFromProp("A011") + user.getUser_name());
			System.out.println(Tool.getValueFromProp("A012") + user.getUser_idNo());

			if (1 == user.getGender()) {
				System.out.println(Tool.getValueFromProp("A013") + "男");
			} else if (2 == user.getGender()) {
				System.out.println(Tool.getValueFromProp("A013") + "女");
			}

			System.out.println(Tool.getValueFromProp("A013") + user.getGender());
			System.out.println(Tool.getValueFromProp("A001") + user.getCard());
			System.out.println(Tool.getValueFromProp("A014") + user.getBalance());

			if (1 == (user.getStatus())) {
				System.out.println(Tool.getValueFromProp("A015") + user.getStatus() + " 正常用户 ");
			} else if (2 == (user.getStatus())) {
				System.out.println(Tool.getValueFromProp("A015") + user.getStatus() + " 销户用户 ");
			} else if (3 == (user.getStatus())) {
				System.out.println(Tool.getValueFromProp("A015") + user.getStatus() + " 锁定用户 ");
			}

			System.out.println("\n");
		}
	}
	
	private void unlockAccountView(AdminService adminService) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		while (true) {
			
			// 显示界面
			System.out.println(Tool.getValueFromProp("A020"));

			// 接受用户输入
			String card = Tool.input();

			boolean bln = adminService.queryAccount(card,3);
			
			if (bln) {
				adminService.unlockAccount(card);
				System.out.println(Tool.getValueFromProp("A021"));
				return;
			}
			
			System.out.println(Tool.getValueFromProp("A030"));
		}
		
	}
	
	private void resetPasswordView(AdminService adminService) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		while (true) {
			
			// 显示界面
			System.out.println(Tool.getValueFromProp("A020"));

			// 接受用户输入
			String card = Tool.input();

			boolean bln = adminService.queryAccount(card,1);
			
			if (bln) {
				
				System.out.println(Tool.getValueFromProp("A027"));
				String user_password = Tool.input();
				
				//更新数据库
				adminService.resetPassword(user_password,card);
				System.out.println(Tool.getValueFromProp("A028"));
				return;
			}
		
			System.out.println(Tool.getValueFromProp("A008"));
				
		}
	}
	
}
