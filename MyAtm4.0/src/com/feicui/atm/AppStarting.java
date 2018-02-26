package com.feicui.atm;

import java.io.IOException;

import com.feicui.atm.core.AbstractView;
import com.feicui.atm.core.RequestMap;

/**
 * 程序开始运行
 * @author 宁强
 * 创建时间：2018-2-5 10:52:45
 */
public class AppStarting {
	
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		RequestMap requestMap = new RequestMap();
		
		//设置为主登录界面路径
		requestMap.setDisPath("LoginView");
		
		while (true) {
			
			AbstractView abstractView = requestMap.forword();
			abstractView.view(requestMap);
			
		}
	}
}
