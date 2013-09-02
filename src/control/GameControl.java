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
		// 游戏数据源
		gameDto = new GameDto();
		// 创建游戏逻辑块(安装游戏数据源)
		gService = new GameTetris(gameDto);
		// 从数据库读取数据
		dbData = configureInit(GameConfig.getDATA().getDataDB());
		// 将数据库记录存入dto
		gameDto.setDbRecord(dbData.loadData());
		// 从本地文件读取数据
		localData = configureInit(GameConfig.getDATA().getDataLocal());
		// 将本地文件记录存入
		gameDto.setLocalRecord(localData.loadData());
		
		// 游戏面板
		panel = new JPanelGame(this, gameDto);
		// 创建游戏窗口，安装游戏面板
		JFrameGame frameGame = new JFrameGame(panel);
		
		// 初始化用户配置窗口
		frameConfig = new JFrameConfig("setting", this);
		this.getFrameConfig().relativeTo(frameGame);
		// 初始化用户配置窗口
		frameRecord = new JFrameRecord(this);
		this.frameRecord.relativeTo(frameGame);
		
		// 初始化游戏按键设置
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
	
	/* 根据keycode，获得方法名，利用反射调用对应按键方法 */
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
	 * 设置按钮事件 显示设置界面
	 */
	public void setting() {
		this.frameConfig.getJLabel().setText("");
		this.frameConfig.getJLabel().setIcon(null);
		this.frameConfig.setVisible(true);
		
	}
	
	/**
	 * 开始按钮事件 启动游戏
	 */
	public void start() {
		// 游戏初始化
		gService.startGame();
		
		// 面板按钮不可点击
		panel.buttonSwitch(false);
		// 關閉窗口
		frameConfig.setVisible(false);
		frameRecord.setVisible(false);
		// 创建调用方块下落方法的线程
		gameThread = new Thread(new FallingThread());
		gameThread.start();

	}
	
	/**
	 * 子窗口关闭事件
	 */
	public void setOver() {
		this.panel.repaint();
		setKeyBorad();
	}
	
	/**
	 * 失败之后事件处理
	 * 
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

	/* 获取当前玩家名，和分数，刷新记录 */
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
