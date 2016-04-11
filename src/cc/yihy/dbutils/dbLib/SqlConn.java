package cc.yihy.dbutils.dbLib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;


public class SqlConn {
    public static String  sqlShow;
	private static String user; // 用户名
	private static String password; // 密码
	private static String driver; // 数据库
	private static String url;


	private static DataSource dataSource;
	private static ThreadLocal<Connection> threadlocal = new ThreadLocal<Connection>();

	private SqlConn() {
		
	}


	static {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("Config/SqlConn.ini"));
			driver = props.getProperty("driverClassName");
			url = props.getProperty("url");
			user = props.getProperty("username");
			password = props.getProperty("password");
			sqlShow = props.getProperty("sqlshow");
			Class.forName(driver);
			try {
				threadlocal.set(DriverManager
						.getConnection(url, user, password));
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			System.err.println("找不到SQL连接资源文件！");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("请确认SQL连接资源文件内键值正确");
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Connection conn = threadlocal.get();
		if (conn == null || conn.isClosed()) {
			Class.forName(driver);
			try {
				threadlocal.set(DriverManager
						.getConnection(url, user, password));
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		return conn;
	}

	public static void CloseConn() {
		Connection conn = threadlocal.get();
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			threadlocal.set(null);
		}
	}
}
