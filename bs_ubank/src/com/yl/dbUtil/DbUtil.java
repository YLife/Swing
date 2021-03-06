package com.yl.dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.yl.interfaces.dao.ResultMapper;

public class DbUtil {
	//加载驱动(只需一次即可)
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取数据库连接
	 * @return connection
	 */
	public Connection getConnection(){
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?user=root&password=YL635901");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 关闭数据库连接
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public void close(ResultSet rs , Statement stmt , Connection conn){
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行增删改操作
	 * @return 受影响的行
	 */
	public int excuteUpdate(String sql , Object... params){
		Connection conn = this.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					stmt.setObject(i + 1, params[i]);
				}
			}
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(null, stmt, conn);
		}
		return 0;
	}
	
	public <E> List<E> excuteQuery(String sql , ResultMapper<E> rm , Object... params){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			stmt = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					stmt.setObject(i + 1, params[i]);
				}
			}
			rs = stmt.executeQuery();
			List<E> list = new ArrayList<E>();
			//解析结果集
			while (rs.next()) {
				E entity = rm.resultMapping(rs);
				list.add(entity);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(rs, stmt, conn);
		}
		return null;
	} 
}
