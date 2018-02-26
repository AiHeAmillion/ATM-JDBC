package com.feicui.atm.dao;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.feicui.atm.anno.Column;
import com.feicui.atm.anno.Table;
import com.feicui.atm.entity.Running_tab;
import com.feicui.atm.entity.User;
import com.feicui.atm.utils.Tool;

/**
 * 抽象的数据操作类父类
 * @author 宁强
 * 创建时间：2018-2-14 23:03:34
 */
public abstract class AbstractDao<T> {
	
	public AbstractDao() {
		
	}
	
	/**
	 * sql插入   用于向数据库新增数据
	 * @param tObj 与数据库表相对应的类的实例
	 * @param objects 需要插入的数据，不定个数
	 */
	public void save (T tObj,Object ...objects) {
		
		Table table = tObj.getClass().getAnnotation(Table.class);
		
		StringBuilder sbSql = new StringBuilder();
		
		sbSql.append("insert into ");
		
		// 表名
		sbSql.append(table.value());
		sbSql.append("(");
		Field[] fileds = tObj.getClass().getDeclaredFields();
		for (Field field : fileds) {
			
			//设置true可以反射出私有属性
			field.setAccessible(true);
			Column column = field.getAnnotation(Column.class);
			sbSql.append(column.value()).append(",");
		}
		sbSql.deleteCharAt(sbSql.length()-1);
		sbSql.append(" ) values ( ");
		
		for (Field field : fileds) {
			field.setAccessible(true);
			sbSql.append("?, ");
		}
		sbSql.deleteCharAt(sbSql.length()-2);
		sbSql.append(" ) ");
		
		//下面链接数据库
		Connection con = Tool.connectMySql();
		
		try {
			
			//获取执行的sql的PrepareStatement对象
			PreparedStatement preparedStatement = con.prepareStatement(sbSql.toString());
			
			int count = 1;
			
			//给preparedStatement的？赋值
			for (Object value : objects) {
				
				preparedStatement.setObject(count++, value);
				
			}
			
			//执行sql语句，返回结果——新增
			preparedStatement.executeUpdate();
			
			//关闭资源
			preparedStatement.close();
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * sql查询   根据条件查出具体的一条数据
	 * @param tObj 与数据库表相对应的类的实例
	 * @param objects 分为两部分 1.sql的where语句部分 2.查询条件的数值
	 * 				     查询的条件的数值  LoginQuerySql时 1.账户或者身份证号 2.密码 3.状态
	 * 							   Running_tabQuerySql时 账号
	 *                             StatusQuerySql是 状态代码
	 *                             User_idNoQuerySql时 是身份证号
	 *                             TransferMoneySql时  是账号和状态 
	 * @return 根据条件查出的user对象 的map集合 如果没有查到map.isEmpty()为true 查到了为false 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public HashMap<Integer, Object> query(T tObj,Object ...objects) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		
		Table table = tObj.getClass().getAnnotation(Table.class);
		
		StringBuilder sbSql = new StringBuilder();
		
		sbSql.append(" select ");
		
		// 字段名/列名/属性名
		Field[] fields = tObj.getClass().getDeclaredFields();
		for (Field field : fields) {
			
			//设置true可以反射出私有属性
			field.setAccessible(true);
			
			Column column = field.getAnnotation(Column.class);
			sbSql.append(column.value()).append(",");
		}
		
		sbSql.deleteCharAt(sbSql.length()-1);
		sbSql.append(" from ");
		
		//表名及where条件
		sbSql.append(table.value()).append(" ").append(objects[0]);
		
		//下面链接数据库
		Connection con = Tool.connectMySql();

		try {
			
			//获取执行sql的preparedStatement对象
			PreparedStatement preparedStatement = con.prepareStatement(sbSql.toString());
			
			for (int i = 1; i < objects.length; i++) {
				
				preparedStatement.setObject(i, objects[i]);
			}
			
			//执行sql语句，返回结果——查询
			ResultSet rs = preparedStatement.executeQuery();
			
			HashMap<Integer, Object> resultMap = new HashMap<Integer,Object>();
			
			if ("userinfo".equals(table.value())) {
				
				//把查到的数据赋到一个新user对象中
				while (rs.next()) {
					
					//用反射写成通用
					User user = (User)Tool.reflectInstanceFromProp("User");
					user.setId(rs.getInt("id"));
					user.setUser_name(rs.getString("user_name"));
					user.setUser_idNo(rs.getString("user_idNo"));
					user.setGender(rs.getInt("gender"));
					user.setBirthday(rs.getString("birthday"));
					user.setAddress(rs.getString("address"));
					user.setRemark(rs.getString("remark"));
					user.setBalance(rs.getDouble("balance"));
					user.setCard(rs.getString("card"));
					user.setUser_password(rs.getString("user_password"));
					user.setStatus(rs.getInt("status"));
					
					resultMap.put(user.getId(), user);
				}
				
			}else {
				
				//把查到的数据赋到一个新user对象中
				while (rs.next()) {
					
					//用反射写成通用
					Running_tab running_tab = (Running_tab)Tool.reflectInstanceFromProp("Running_tab");
					running_tab.setId(rs.getInt("id"));
					running_tab.setCard(rs.getString("card"));
					running_tab.setTarget_account(rs.getString("target_account"));
					running_tab.setTrade_type(rs.getInt("trade_type"));
					running_tab.setTrade_date(rs.getString("trade_date"));
					running_tab.setTrade_money(rs.getDouble("trade_money"));
					running_tab.setCard_balance(rs.getDouble("card_balance"));
					
					resultMap.put(running_tab.getId(), running_tab);
				}
				
			}
			
			//关闭资源 先用的后关掉
			rs.close();
			preparedStatement.close();
			con.close();
			
			//返回这个对象的集合
			return resultMap;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * sql更新   用于更新数据库数据
	 * @param tObj 与数据库表相对应的类的实例
	 * @param objects  1.需要更新的列名(user_password/balance/status) 2.需要更新的数据 3.更新的账户
	 */
	public void update(T tObj,Object ...objects) {
		
		Table table = tObj.getClass().getAnnotation(Table.class);
		
		StringBuilder sbSql = new StringBuilder();
		
		//update + 表名 + set
		sbSql.append(" update ").append(table.value()).append(" set ");
		
		//set的属性
		sbSql.append(objects[0]+" = ? ");
		
		//where条件
		sbSql.append(" where card = ?");
		
		//下面链接数据库
		Connection con = Tool.connectMySql();
		
		try {
			
			//获取执行sql的preparedStatement对象
			PreparedStatement preparedStatement = con.prepareStatement(sbSql.toString());
			
			//给preparedStatement的？赋值
			preparedStatement.setObject(1, objects[1]);
			preparedStatement.setObject(2, objects[2]);
			
			//执行sql语句，返回结果——新增
			preparedStatement.executeUpdate();
			
			//关闭资源
			preparedStatement.close();
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 找到数据库表最大的id
	 * @return 返回最大id
	 */
	public int maxIdQuery(T tObj,Object ...objects) {
		
		Table table = tObj.getClass().getAnnotation(Table.class);
		
		StringBuilder sbSql = new StringBuilder();
		
		sbSql.append("select max(id) from ");
		
		//添加表名
		sbSql.append(table.value());
		
		//下面链接数据库
		Connection con = Tool.connectMySql();
		
		try {
			
			//获取执行sql的preparedStatement对象
			PreparedStatement preparedStatement = con.prepareStatement(sbSql.toString());
			
			//执行sql语句，返回结果——查询
			ResultSet rs = preparedStatement.executeQuery();
			
			int id=0;
			if (rs.next()) {
				
				//查到最大id赋值id
				id = rs.getInt("MAX(id)");
				
			}
			
			//关闭资源 先用的后关掉
			rs.close();
			preparedStatement.close();
			con.close();
			
			//返回id
			return id;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return 0;
		
	}
}
