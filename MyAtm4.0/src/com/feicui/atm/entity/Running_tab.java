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
 * 交易流水类，包含流水的各项属性
 * @author 宁强
 * 创建时间：2018-2-19 00:00:04
 */
@Table(value = "running_tab")
public class Running_tab {
	
	//交易流水号
	@Column("id")
	private int id;
	
	//源账户：业务发起账户
	@Column("card")
	private String card;
	
	//目标账户：业务接受账户
	@Column("target_account")
	private String target_account;
	
	//业务类型：1:存款 2:取款 3: 转账-支出  4:转账-收入
	@Column("trade_type")
	private int trade_type;
	
	//交易时间
	@Column("trade_date")
	private Date trade_date;
	
	//交易金额
	@Column("trade_money")
	private double trade_money;
	
	//源账户余额
	@Column("card_balance")
	private double  card_balance;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getTarget_account() {
		return target_account;
	}

	public void setTarget_account(String target_account) {
		this.target_account = target_account;
	}

	public int getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(int trade_type) {
		this.trade_type = trade_type;
	}

	public String getTrade_date() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(trade_date);
	}

	public void setTrade_date(String trade_date) {
		
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			this.trade_date = fmt.parse(trade_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public double getTrade_money() {
		return trade_money;
	}

	public void setTrade_money(double trade_money) {
		this.trade_money = trade_money;
	}

	public double getCard_balance() {
		return card_balance;
	}

	public void setCard_balance(double card_balance) {
		this.card_balance = card_balance;
	}
	
	/**
	 * 把流水信息转换成字符串
	 * @return 
	 * @throws IOException
	 */
	public String getString() throws IOException {
		
		String statusString = "转账业务-收入";
		
		if (1 == getTrade_type()) {
			statusString = "存款业务";
		} else if (2 == getTrade_type()) {
			statusString = "取款业务";
		} else if (3 == getTrade_type()) {
			statusString = "转账业务-支出";
		} 
		
		return 
				
		Tool.getValueFromProp("L202") + getId()+"\n"+
		Tool.getValueFromProp("L003") + getCard()+"\n"+
		Tool.getValueFromProp("L004") + getTarget_account()+"\n"+
		
		Tool.getValueFromProp("L005") + statusString+"\n"+
		Tool.getValueFromProp("L006") + getTrade_date()+"\n"+
		Tool.getValueFromProp("L007") + getTrade_money()+"\n"+
		Tool.getValueFromProp("L008") + getCard_balance()+"\n";

	}
}
