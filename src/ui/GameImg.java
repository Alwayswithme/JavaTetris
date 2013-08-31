package ui;

import java.awt.Image;
import javax.swing.ImageIcon;
import config.GameConfig;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class GameImg {
	 /* -- can not use this Constructors -- */
	private GameImg() {};

	//图片主路径
	public static String GRAPHICS_PATH = "Graphics/";

	//皮肤默认路径
	private static String path = "default/";

	/**
	 * 窗口素材
	 */
	public static Image WINDOW = null;
	
	/**
	 * 矩形值槽
	 */
	public static Image RECT = null;
	/**
	 * 关于
	 */
	public static Image ABOUT = null;
	/**
	 * 标题(分数)
	 */
	public static Image POINT = null;
	/**
	 * 标题(消行)
	 */
	public static Image RMLINE = null;
	/**
	 * 标题(本地记录)
	 */
	public static Image LOCAL = null;
	/**
	 * 标题(等级)
	 */
	public static Image LV = null;
	/**
	 * 标题(数据库)
	 */
	public static Image DB = null;
	/**
	 * 七色方块素材
	 */
	public static Image CUBE = null;
	/**
	 * 数字素材
	 */
	public static Image NUM = null;
	/**
	 * 开始按钮
	 */
	public static ImageIcon START = null;
	/**
	 * 设置按钮
	 */
	public static ImageIcon SETTING = null;
	/**
	 * 游戏图标
	 */
	public static Image MAIN_ICON = null;
	/**
	 * 暂停图片
	 */
	public static Image PAUSE = null;
	/**
	 * 提示方块图片
	 */
	public static Image[] HINT_CUBE;
	/**
	 *  背景图片数组
	 */
	public static List<Image> BG_IMG;
	/**
	 * 获得皮肤路径，初始化游戏所需各种图片
	 * @param path 皮肤路径
	 */
	public static void setSkin(String path) {
		/*
		 * 提示图片数组，根据下标获取的图片方块需对应配置文件方块顺序
		 */
		int num = GameConfig.getSYSTEM().getECube().size();
		HINT_CUBE = new Image[num];
		for (int i = 0; i < num ; i++) {
			HINT_CUBE[i] = new ImageIcon(GRAPHICS_PATH + path + "game/" + i + ".png").getImage();
		}
		/*
		 * 背景图片列表，遍历对应文件夹获得
		 */
		File dir = new File(GRAPHICS_PATH + path + "background");
		File[] files = dir.listFiles();
		Arrays.sort(files);
		BG_IMG = new ArrayList<Image>();
		for (File file : files) {
			if (file.isDirectory()) 
				continue;
			BG_IMG.add(new ImageIcon(file.getPath()).getImage());
		}
		//窗口素材
		WINDOW = new ImageIcon(GRAPHICS_PATH + path + "window/Window.png").getImage();
		//矩形值槽
		RECT = new ImageIcon(GRAPHICS_PATH + path + "window/rect.png").getImage();	
		//关于
		ABOUT = new ImageIcon(GRAPHICS_PATH + path + "string/about.png").getImage();
		//标题(分数)
		POINT = new ImageIcon(GRAPHICS_PATH + path + "string/point.png").getImage();
		//标题(消行)
		RMLINE = new ImageIcon(GRAPHICS_PATH + path + "string/rmline.png").getImage();
		//标题(本地记录)
		LOCAL = new ImageIcon(GRAPHICS_PATH + path + "string/local.png").getImage();
		//标题(等级)
		LV = new ImageIcon(GRAPHICS_PATH + path + "string/level.png").getImage();
		//标题(数据库)
		DB = new ImageIcon(GRAPHICS_PATH + path + "string/db.png").getImage();
		//七色方块素材
		CUBE = new ImageIcon(GRAPHICS_PATH + path + "game/rect.png").getImage();
		//数字素材
		NUM = new ImageIcon(GRAPHICS_PATH + path + "string/num.png").getImage();
		//开始按钮
		START = new ImageIcon(GRAPHICS_PATH + path + "string/start.png");
		//设置按钮
		SETTING = new ImageIcon(GRAPHICS_PATH + path + "string/setting.png");
		//游戏图标
		MAIN_ICON = new ImageIcon(GRAPHICS_PATH + path + "window/icon.png").getImage();
		//暂停图片
		PAUSE = new ImageIcon(GRAPHICS_PATH + path + "string/pause.png").getImage();
	}
	static {
		//初始化默认皮肤
		setSkin(path);
	}
}
