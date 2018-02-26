package com.feicui.atm.core;

import java.io.IOException;
import java.util.HashMap;

/**
 * 业务接口
 * @author 宁强
 * 创建时间：2018-2-9 21:34:39
 */
public interface IService {
	
	//账户密码验证  管理员|用户
	boolean login(String card,String user_password) throws IOException;
	
	//开户  管理员
	void openAccount(Object...objects) throws IOException;
	
	//销户 管理员
	void clossAccount(Object...objects) throws IOException;
	
	//查看账户 1.正常 2.销户 3.锁定 | 用户个人查看信息
	HashMap<Integer, Object> queryOne(Object...objects) throws IOException;
	
	//解锁锁定用户 管理员
	void unlockAccount(Object...objects) throws IOException;
	
	//重置密码 管理员
	void resetPassword(Object...objects) throws IOException;
	
	//存款 用户
	void saveMoney(Object...objects) throws IOException;
	
	//取款 用户
	void drawMoney(Object...objects) throws IOException;
	
	//转账 用户
	int transferMoney(Object...objects) throws IOException;
	
	
}
