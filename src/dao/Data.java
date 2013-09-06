package dao;
import java.util.List;
import dto.Player;

public interface Data {
	/**
	 * load data
	 */
	List<Player> loadData();

	/**
	 * save data
	 */
	void saveData(Player player);
}
