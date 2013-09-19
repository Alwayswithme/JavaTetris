package dao;
import java.util.List;
import dto.Player;

public interface Data {
	/**
	 * load data from local file or database
	 */
	List<Player> loadData();

	/**
	 * save data to file or database
	 */
	void saveData(Player player);
}
