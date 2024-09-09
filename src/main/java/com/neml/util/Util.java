package com.neml.util;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
@Component
public class Util implements DbConnectionUtil {
	private static final Logger log = LoggerFactory.getLogger("Util");
	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;
	public static MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
	public static HikariPoolMXBean poolProxy;

	
	
	
Environment env;
	
	@Autowired
	public void DbConnection(@Autowired Environment env) {
		this.env=env;
		this.DBUtil();
	}

	public void DBUtil(){
		try {
			log.info("Initializing Database Pool");
			config.setJdbcUrl(env.getProperty("spring.datasource.url"));
	        config.setUsername(env.getProperty("spring.datasource.username"));
	        config.setPassword(env.getProperty("spring.datasource.password"));
	        config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	        config.addDataSourceProperty("cachePrepStmts" ,"true");
	        config.addDataSourceProperty("prepStmtCacheSize","250");
	        config.addDataSourceProperty("prepStmtCacheSqlLimit","2048");
	        config.setRegisterMbeans(true);
	        config.setPoolName("UserService");
	        ds = new HikariDataSource(config);
			Connection con = ds.getConnection();
			con.close();
			ObjectName poolName = new ObjectName("com.zaxxer.hikari:type=Pool ("+ds.getPoolName()+")");
			poolProxy = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);
			log.info("Initialized Database Pool");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	
	
	
	
	
	
	
	
	
	
	public static boolean isNeitherNullNorEmpty(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String && ((String) obj).trim().equals("")) {
			return false;
		}
		if (obj instanceof List<?> && ((List<?>) obj).isEmpty()) {
			return false;
		}
		return true;
	}

	
	
	



	public ResultSet select(String query, List<Object> param, Connection con, Logger logger) {
		long executionStartTime = 0;
		long executionEndTime = 0;
		StringBuffer logString = null;
		ResultSet rs = null;
		try {
			PreparedStatement prepstmt = null;
			if (logger != null) {
				// logger.write(query);
				logger.info("select() :: Query=" + query);
			}
			logString = new StringBuffer("DBUser: " + con.getMetaData().getUserName() + " | " + query)
					.append("  with values ::");
			prepstmt = con.prepareStatement(query);

			if (Util.isNeitherNullNorEmpty(param)) {
				int q = param.size();
				for (int j = 1; j <= q; j++) {
					Object temp = param.get(j - 1);
					logString.append(temp).append("|");
					prepstmt.setObject(j, temp);
				}
			}

			if (logger != null) {
				// String temp = logString.toString();
				// log.info(temp);
				logger.info("select() :: " + logString.toString());
			}
			executionStartTime = System.currentTimeMillis();
			rs = prepstmt.executeQuery();
			executionEndTime = System.currentTimeMillis();
		} catch (Exception e) {
			e.printStackTrace();
			executionEndTime = System.currentTimeMillis();
			if (logger != null) {
				// log.error("Exception while select : " + e.getMessage());
				logger.info("select() :: Exception while select :" + e.getMessage());
			}
		} finally {
			try {
				if (logger != null) {
					// log.info("Query Execution time :" + (executionEndTime-executionStartTime)+ "
					// milliseconds");
					logger.info("select() :: " + "Query Execution time :" + (executionEndTime - executionStartTime)
							+ " milliseconds");
				}
				if (param != null)
					param.clear();
				param = null;
				query = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rs;
	}
	
	
	
	public  Connection getDBConnection(String str,Logger logger) throws Exception {
		log.info("Before Getting Database Connection :: Method and FileName :: "+str+" :: Active : " + poolProxy.getActiveConnections() + " Waiting : "
				+ poolProxy.getThreadsAwaitingConnection() + " Total : " + poolProxy.getTotalConnections());
		Connection conn = ds.getConnection();
		log.info("After Getting Database Connection :: Active : " + poolProxy.getActiveConnections() + " Waiting : "
				+ poolProxy.getThreadsAwaitingConnection() + " Total : " + poolProxy.getTotalConnections());
		return conn;
	}

	public  void closeConnection(Connection conn,String str,Logger logger) {
		if (conn != null) {
			logger.info("Inside closeConnection :: Method and FileName :: "+str);
			try {
				conn.close();
				logger.info("Closed connection :: Method and FileName :: "+str);
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Error in closeRsPstmtConn " + e.getMessage());
			}
		}
	}

	
	
}
