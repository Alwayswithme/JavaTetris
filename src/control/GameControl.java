package control;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import service.GameService;
import service.GameTetris;
import ui.JFrameGame;
import ui.JPanelGame;
import ui.cfg.CfgResources;
import ui.cfg.JFrameConfig;
import ui.cfg.JFrameRecord;
import config.DataInterfaceConfig;
import config.GameConfig;
import dao.Data;
import dto.GameDto;
import dto.Player;

/**
 * Controler
 * control the key event, get the data record,
 * control the screen and game logic
 */
public class GameControl {
	/**
	 * interface to access the database
	 */
	private Data dbData;
	/**
	 * interface to visit the local file
	 * and get the player record
	 */
	private Data localData;
	/**
	 * the game main JPanel 
	 */
	private JPanelGame panel;
	/**
	 * DTO to get the data
	 */
	private GameDto gameDto;
	/**
	 * Service to control game
	 */
	private GameService gService;
	/**
	 * frame use for the game config
	 */
	private JFrameConfig frameConfig;
	/**
	 * frame use for enter info
	 * when new record show
	 */
	private JFrameRecord frameRecord;
	/**
	 * define the keycode go with what method
	 * such as move right, rotate
	 */
	private Map<Integer, Method> actionMap;
	/**
	 * the game thread to control
	 * the terrominoe falling
	 */
	private Thread gameThread = null;

	public GameControl() {
		// DataTransferObject aka DTO
		gameDto = new GameDto();
		// game logic service
		gService = new GameTetris(gameDto);
		// reading data from database
		dbData = configureInit(GameConfig.getDATA().getDataDB());
		// set the database record to DTO
		gameDto.setDbRecord(dbData.loadData());
		// reading data from localfile
		localData = configureInit(GameConfig.getDATA().getDataLocal());
		// set the local record to DTO
		gameDto.setLocalRecord(localData.loadData());
		
		// let the panel access to DTO(DataTransferObject)
		panel = new JPanelGame(this, gameDto);
		// use the panel to initialize JFrame
		JFrameGame frameGame = new JFrameGame(panel);
		
		// initialize the user config window
		frameConfig = new JFrameConfig("setting", this);
		this.getFrameConfig().relativeTo(frameGame);
		// initialize the record window
		frameRecord = new JFrameRecord(this);
		this.frameRecord.relativeTo(frameGame);
		
		// initialize the keyboard setting
		setKeyBorad();
	}
	
	/**
	 * 读取按键设置 方法名String作为按键编码(keycode)与按键事件(method)的中枢
	 */
	public void setKeyBorad() {
		// 存放按键(keycode)和方法名的映射
		actionMap = new HashMap<Integer, Method>();
		try {
			// 从指定配置文件读取配置
			ObjectInputStream ois = new ObjectInputStream(
			                new FileInputStream(CfgResources.FILE));
			@SuppressWarnings( "unchecked")
			// 方法名和按键编码的映射
			HashMap<String, Integer> settings = (HashMap<String, Integer>) ois.readObject();
			ois.close();
			
			// 迭代出方法名和按键编码
			for (Entry<String, Integer> entry : settings.entrySet()) {
				String methodName = entry.getKey();
				int keyCode = entry.getValue();
				actionMap.put(keyCode, gService.getClass().getMethod(methodName));
			}
		} catch (ClassNotFoundException | NoSuchMethodException
		                | SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一个配置记录对象，利用反射创建并实例化
	 * 
	 * @param cfgData
	 *            配置记录对象
	 * @return
	 */
	public Data configureInit(DataInterfaceConfig cfgData) {
		Data data = null;
		try {
			Class<?> clas = Class.forName(cfgData.getClassName());
			// 获得构造器
			Constructor<?> con = clas.getConstructor(HashMap.class);
			// 创建Data类实例，并返回
			data = (Data) con.newInstance(cfgData.getParaMap());
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
		                | InstantiationException | IllegalAccessException
		                | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * according to the keycode to get the
	 * menthod, and use reflect to invoke it
	 */
	public void actionByKeyCode(int keyCode) {
		if (actionMap.containsKey(keyCode))
			try {
				actionMap.get(keyCode).invoke(gService);
			} catch (IllegalAccessException | IllegalArgumentException
			                | InvocationTargetException
			                | SecurityException e) {
				e.printStackTrace();
			}
		// 重新刷新画面
		panel.repaint();
	}
	
	/**
	 * setting button event and
	 * show a setting window for
	 * set the key and skin customly
	 */
	public void setting() {
		this.frameConfig.getJLabel().setText("");
		this.frameConfig.getJLabel().setIcon(null);
		this.frameConfig.setVisible(true);
	}
	
	/**
	 * start button event and let
	 * the game start
	 */
	public void start() {
		//let the service to initialize game
		gService.startGame();
		
		// make the button cannot be pressed
		panel.buttonSwitch(false);
		// close setting window
		frameConfig.setVisible(false);
		frameRecord.setVisible(false);
		// create the thread to get the tetrominoes falling automatically
		gameThread = new Thread(new FallingThread());
		gameThread.start();
	}
	
	/**
	 * when setting over, to get the
	 * focus and set the keyboard
	 */
	public void setOver() {
		this.panel.repaint();
		setKeyBorad();
	}
	
	/**
	 * deal with 
	 * @return
	 */
	private void afterLose() {
		// 如果打破记录，显示保存得分窗口
		isNewRecord(gameDto.getStatus().getScore());
		
		// 使按钮可以点击
		panel.buttonSwitch(true);
	}
	
	private void isNewRecord(int finalScore) {
		// 与本地或数据库第五名分数进行比较
		if (finalScore > gameDto.getLocalRecord().get(4).getScore() ||
		                finalScore > gameDto.getDbRecord().get(4).getScore()) {
			frameRecord.show(finalScore);
		}
	}

	private class FallingThread implements Runnable {
		@Override
		public void run() {
			panel.repaint();
			while (gameDto.getStatus().isStart()) {
				try {
					Thread.sleep(gameDto.getStatus().getSpeed());
					if (gameDto.getStatus().isPause())
						continue;
					gService.mainAction();
					panel.repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			afterLose();
		}
	}

	/**
	 * get current player name and score
	 * and save it to the database and
	 * local record
	 */
	public void saveScore(String playerName) {
		Player p = new Player(playerName, gameDto.getStatus().getScore());
		// 存入本地磁盘
		localData.saveData(p);
		// 存入数据库
		dbData.saveData(p);
		// 将数据库记录存入dto
		gameDto.setDbRecord(dbData.loadData());
		// 将本地文件记录存入
		gameDto.setLocalRecord(localData.loadData());
		// 刷新畫面
		panel.repaint();
		
	}

	/*  getters  */
	public JPanelGame getPanelGame() {
		return panel;
	}
	
	public JFrameConfig getFrameConfig() {
		return frameConfig;
	}
}
