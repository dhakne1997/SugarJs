package com.neml.util;

import java.sql.Connection;

import org.slf4j.Logger;

public interface DbConnectionUtil {

	Connection getDBConnection(String string, Logger log) throws Exception;

	void closeConnection(Connection conn, String string, Logger log);

}
