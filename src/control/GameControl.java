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
import config.DataConfig;
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
	 * read the keyboard setting
	 * using a keycode as key, and the value is a method
	 * when a key press, then invoke the binding method
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
	 * use the data configure object to get a
	 * new instance of Data (which is a interface in
	 * the dao package.
	 * @param dataInterface  data configure object
	 * @return a Data Access Objects use to get info from
	 * database and localfile
	 */
	public Data configureInit(DataInterfaceConfig dataInterface) {
		Data data = null;
		try {
			// load the class
			Class<?> clas = Class.forName(dataInterface.getClassName());
			// get the constructor
			Constructor<?> con = clas.getConstructor(HashMap.class);
			//  
			data = (Data) con.newInstance(dataInterface.getParaMap());
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
		gService.gameRun();
		
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
	 * deal with situation after lose
	 * @return
	 */
	private void afterLose() {
		isNewRecord(gameDto.getStatus().getScore());	// if it's a new record then show the panel
		panel.buttonSwitch(true);						// let the button can be press
	}
	
	private void isNewRecord(int finalScore) {
		// compare to the database's and local file's record
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
		// save it to database and local disk
		localData.saveData(p);
		// reload the record to dto
		gameDto.setLocalRecord(localData.loadData());
		if (DataConfig.isDBavailable()) {
			dbData.saveData(p);
			gameDto.setDbRecord(dbData.loadData());
		}
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
