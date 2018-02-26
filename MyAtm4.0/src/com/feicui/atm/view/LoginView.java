package com.feicui.atm.view;

import java.io.IOException;

import com.feicui.atm.core.AbstractView;
import com.feicui.atm.core.RequestMap;
import com.feicui.atm.utils.Tool;

/**
 * 登录界面
 * @author 宁强
 * 创建时间：2018-2-9 22:03:02
 */
public class LoginView extends AbstractView {
	
	@Override
	public void view(RequestMap requestMap) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		//读取登录界面
		Tool.readUi("login.txt");
		String input = Tool.input();
		
		if ("1".equals(input)) {
			
			//管理员业务选择界面
			requestMap.setDisPath("AdminMenuView");
			
		}else if ("2".equals(input)) {
			
			//用户业务选择界面
			requestMap.setDisPath("UserMenuView");
			
		}else {
			
			System.out.println(Tool.getValueFromProp("M105"));
			
			//输入 1 2之外，还是此界面
			requestMap.setDisPath("LoginView");
			
		}
		
	}
}
