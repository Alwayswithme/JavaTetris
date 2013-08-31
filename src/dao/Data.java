package dao;
import java.util.List;
import dto.Player;

public interface Data {
	/**
	 * 读取数据
	 */
	List<Player> loadData();

	/**
	 * 存储数据
	 */
	void saveData(Player player);
}
