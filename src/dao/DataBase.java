package dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dto.Player;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase implements Data {
	private final String dbUrl;
	private final String dbUser;
	private final String dbPwd;
	

	private static Connection DB_CONNECT =  null;
	private static PreparedStatement stmt = null;
	private static String LOAD_SQL = "SELECT player, score, type_id FROM user_record WHERE type_id = ? ORDER BY score DESC LIMIT 5";
	private static String SAVE_SQL = "INSERT INTO java_Tetris.user_record(player, score, type_id) VALUES (? , ?, 1)";
	public DataBase(HashMap<String, String> paraMap) {
		try {
	        Class.forName(paraMap.get("driver"));
        } catch (ClassNotFoundException e) {
	        e.printStackTrace();
        }
		dbUrl = paraMap.get("dbUrl");
		dbUser = paraMap.get("dbUser");
		dbPwd = paraMap.get("dbPwd");
	}
	@Override
	public List<Player> loadData() {
		ResultSet result = null;
		List<Player> players = new ArrayList<Player>(5);
		try {
			DB_CONNECT = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			
			stmt = DB_CONNECT.prepareStatement(LOAD_SQL);
			stmt.setInt(1, 1);
			result = stmt.executeQuery();
	        while (result.next()) {
	        	players.add(new Player(result.getString(1), result.getInt(2)));
	        }
        } catch (SQLException e) {
	        //e.printStackTrace();
        }finally {
        	try {
	            if(result != null)
	            	result.close();
	            result = null;
	            if(stmt != null)
	            	stmt.close();
	            stmt = null;
	            if (DB_CONNECT != null)
	            	DB_CONNECT.close();
				DB_CONNECT = null;
            } catch (SQLException e) {
	            e.printStackTrace();
            }
        }
		return players;
	}

	@Override
	public void saveData(Player p) {
		try {
			DB_CONNECT = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			stmt = DB_CONNECT.prepareStatement(SAVE_SQL);
			stmt.setString(1, p.getName());
			stmt.setInt(2, p.getScore());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				stmt = null;
				DB_CONNECT.close();
				DB_CONNECT = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}