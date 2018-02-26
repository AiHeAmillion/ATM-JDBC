package com.feicui.atm.entity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.feicui.atm.anno.Column;
import com.feicui.atm.anno.Table;
import com.feicui.atm.utils.Tool;

/**
 * 用户类，包含用户的各种属性
 * @author 宁强
 * 创建时间：2018-2-5 10:47:25
 */
@Table(value = "userinfo")
public class User {
	
	//账户id
	@Column("id")
	private int id;
	
	//用户姓名
	@Column("user_name")
	private String user_name;
	
	//用户身份证号
	@Column("user_idNo")
	private String user_idNo;
	
	//性别 1:男 2：女
	@Column("gender")
	private int gender;
	
	//出生日期
	@Column("birthday")
	private Date birthday;
	
	//地址
	@Column("address")
	private String address;
	
	//备注
	@Column("remark")
	private String remark;
	
	//余额
	@Column("balance")
	private double balance;
	
	//银行卡号
	@Column("card")
	private String card;
	
	//银行密码
	@Column("user_password")
	private String user_password;
	
	//账户状态 0：管理员 1：正常用户 2：销户 3：锁定
	@Column("status")
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_idNo() {
		return user_idNo;
	}

	public void setUser_idNo(String user_idNo) {
		this.user_idNo = user_idNo;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(birthday);
		
	}

	public void setBirthday(String birthday) {
		
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			this.birthday = fmt.parse(birthday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int i) {
		this.status = i;
	}
	
	/**
	 * 用户业务专用
	 * 返回个人信息的字符串形式
	 * @return
	 * @throws IOException
	 */
	public String getString() throws IOException {
		
		String sex = "女";
		
		if(1 == getGender()) {
			sex = "男";
		}
		
		return 
				
			// 个人信息
			Tool.getValueFromProp("C003") + getId()+"\n"+
			Tool.getValueFromProp("C004") + getUser_name()+"\n"+
			Tool.getValueFromProp("C005") + sex+"\n"+
			Tool.getValueFromProp("C006") + getCard()+"\n"+
			Tool.getValueFromProp("C007") + getUser_password()+"\n"+
			Tool.getValueFromProp("C008") + getBalance()+"\n"+
			Tool.getValueFromProp("C009") + getBirthday()+"\n"+
			Tool.getValueFromProp("C010") + getUser_idNo()+"\n"+
			Tool.getValueFromProp("C011") + getAddress()+"\n"+
			Tool.getValueFromProp("C012") + getRemark()+"\n";
		
	}
}
