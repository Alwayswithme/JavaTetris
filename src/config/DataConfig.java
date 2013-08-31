package config;

import java.io.Serializable;
import org.dom4j.Element;

/**
 * 元素data的配置,可通过get方法获得嵌套在data中的dataDB和dataLocal两个元素的配置
 * @author phx
 *
 */
public class DataConfig implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = 6001674074008029775L;
	/**
	 * dataDB元素配置
	 * 存储有驱动、连接、用户名、密码等信息
	 */
	private DataInterfaceConfig dataDB;
	/**
	 * dataLocal元素配置
	 * 存储玩家记录信息保存路径
	 */
	private DataInterfaceConfig dataLocal;
	
	/*
	 * 记录显示条数
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
}
