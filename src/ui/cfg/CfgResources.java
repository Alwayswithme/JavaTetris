package ui.cfg;

import java.awt.Image;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class CfgResources {
	//按键设置配置文件相对路径
	public static final String FILE = "data/Setting.dat";
	// 
	public static final String data_path = "data" + File.separator + "pic" + File.separator;
	/* 设置面板需要用的的图标   */
	
	/* 上下左右箭头图标 */
	public static final Icon UP = getIcon("up.png");
	public static final Icon DOWN = getIcon("down.png");
	public static final Icon LEFT = getIcon("left.png");
	public static final Icon RIGHT = getIcon("right.png");
	
	/* 额外功能图标  */
	public static final Icon EXTRA1 = getIcon("1.png");
	public static final Icon EXTRA2 = getIcon("2.png");
	public static final Icon EXTRA3 = getIcon("3.png");
	public static final Icon EXTRA4 = getIcon("4.png");
	
	/* tabpanel 图标 */
	public static final Icon SKIN = getIcon("skin.png");
	public static final Icon CTRL = getIcon("control.png");
	
	/* 按钮图标 */
	public static final Icon OK = getIcon("ok.png");
	public static final Icon CANCEL = getIcon("cancel.png");
	public static final Icon APPLY = getIcon("apply.png");
	
	public static final Image CTRL_IMG = new ImageIcon("data/pic/control.png").getImage();
	
	
	public static Icon getIcon(String iconName) {
		
		Icon icon = new ImageIcon(data_path + iconName);
		return icon;
	}
}
