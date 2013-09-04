package config;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * this class save all the config object
 * that use to create the game's visual content
 * @author phx
 */

public class GameConfig implements Serializable {
    private static final long serialVersionUID = 2906952047756985068L;
	/**
	 * use to decied the JFrame
	 */
	private static FrameConfig FRAME = null;
	/**
	 * use to connect data interface
	 */
	private static DataConfig DATA = null;
	/**
	 * configure the terrominoe's look and
	 * bonus when remove line
	 */
	private static SystemConfig SYSTEM = null;

	/**
	 * true to turn on debug mode
	 */
	private static final boolean IS_DEBUG = true;

	static {
		if(IS_DEBUG) {
			// in debug mode
			// read the configure object through the xml file
			//XML reader
			SAXReader reader = new SAXReader();
			//XML file
			Document doc = null;
			Element game = null;
			try {
				doc = reader.read("data/cfg.xml");
				game = doc.getRootElement();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			//frame element for UI setting 
			FRAME = new FrameConfig(game.element("frame"));
			//data element for data setting
			DATA = new DataConfig(game.element("data"));
			//system element for system setting
			SYSTEM = new SystemConfig(game.element("system"));
		}else {
			//not in debug mode
			//so read the configure object in the file
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream("data/game_config.dat"));
				FRAME = (FrameConfig) ois.readObject();
				DATA = (DataConfig) ois.readObject();
				SYSTEM =  (SystemConfig) ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 获得窗口配置
	 * @return FrameConfig
	 */
	public static FrameConfig getFRAME() {
		return FRAME;
	}
	/**
	 * 获得游戏数据配置
	 * @return DataConfig
	 */
	public static DataConfig getDATA() {
		return DATA;
	}
	/**
	 * 获得系统配置
	 * @return SystemConfig
	 */
	public static SystemConfig getSYSTEM() {
		return SYSTEM;
	}
	/**
	 * this method is to create a file
	 * to store the necessary object
	 */
	public static void main(String... args) throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/game_config.dat"));
		oos.writeObject(FRAME);
		oos.writeObject(DATA);
		oos.writeObject(SYSTEM);
		oos.close();
	}
}
