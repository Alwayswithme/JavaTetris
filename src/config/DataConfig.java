package config;

import java.io.Serializable;
import org.dom4j.Element;

/**
 * 元素data的配置,可通过get方法获得嵌套在data中的dataDB和dataLocal两个元素的配置
 * @author phx
 *
 */
public class DataConfig implements Serializable{
    private static final long serialVersionUID = 6001674074008029775L;
	/**
	 * dataDB element configure
	 * store the info about driver, connector, username and password
	 */
	private DataInterfaceConfig dataDB;

	/**
	 * when database is not available
	 * then can turn it to false
	 */
	public static Boolean DBavailable = true;
	/**
	 * dataLocal element configure
	 * store the player's record save path
	 */
	private DataInterfaceConfig dataLocal;
	/*
	 * define the numbers of record show in
	 * the game panel
	 */
	private final int maxRow;
	public int getMaxRow() {
		return maxRow;
	}

	public DataConfig(Element data) {
		maxRow = Integer.parseInt(data.attributeValue("max_row"));
		dataDB = new DataInterfaceConfig(data.element("dataDB"));
		dataLocal = new DataInterfaceConfig(data.element("dataLocal"));
    }

	public DataInterfaceConfig getDataDB() {
		return dataDB;
	}
	
	public DataInterfaceConfig getDataLocal() {
		return dataLocal;
	}

	public static Boolean isDBavailable() {
		return DBavailable;
	}

	public static void DBnotAvailable() {
		DBavailable = false;
	}
}
