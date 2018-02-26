package com.feicui.atm.core;

import java.io.IOException;

import com.feicui.atm.utils.Tool;

/**
 * @author 宁强
 * 接受用户请求，调用不同的界面
 * 创建时间：2018-2-9 21:35:35
 */
public class RequestMap {
	
	//界面路径
	private String disPath;
	
	public void setDisPath(String disPath) {
		this.disPath = disPath;
	}
	
	public String getDisPath() {
		return disPath;
	}
	
	/**
	 * 根据页面类路径获取该类的实例
	 * @return 该页面类的实例对象
	 */
	public AbstractView forword() {
		
		AbstractView abstractView = null;
		
		try {
			
			abstractView = (AbstractView)Tool.reflectInstanceFromProp(disPath);
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return abstractView;
	}
	
}
