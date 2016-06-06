package models;

import java.sql.Connection;
import java.sql.SQLException;

public class Model {

	public static void closeConnection(Connection conn) throws SQLException{
		if(conn != null){
			conn.close();
		}
	}
}
