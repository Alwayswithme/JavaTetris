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
 * game configure
 * 储存XML配置文件中game元素的嵌套
 * @author phx
 */

public class GameConfig implements Serializable {
	/**
	 * 
	 */


    private static final long serialVersionUID = 2906952047756985068L;
	private static FrameConfig FRAME = null;
	private static DataConfig DATA = null;
	private static SystemConfig SYSTEM = null;

	private static final boolean IS_DEBUG = true;

	static {
		if(IS_DEBUG) {
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
			//界面配置 frame元素
			FRAME = new FrameConfig(game.element("frame"));
			//数据访问配置 data元素
			DATA = new DataConfig(game.element("data"));
			//系统配置  system元素
			SYSTEM = new SystemConfig(game.element("system"));
		}else {
			ObjectInputStream ois = null;
            try {
	            ois = new ObjectInputStream(new FileInputStream("data/game_config.dat"));
	            //System.out.println(config.getFRAME().getCubeSize());
	            
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
	public static void main(String... args) throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/game_config.dat"));
		oos.writeObject(FRAME);
		oos.writeObject(DATA);
		oos.writeObject(SYSTEM);
		oos.close();

   
	}
}