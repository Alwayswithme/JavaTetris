package ui.cfg;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * 封装添加到JTabbedPanel的按键设置Panel
 * 
 */
@SuppressWarnings( "serial")
public class SettingPanel extends JPanel {
	
	/**
	 * 二位数组，两组标签
	 */
	private static JLabel[][] labels = {
		//第一组方法说明标签
		{new JLabel("↑", CfgResources.UP,JLabel.CENTER),
		new JLabel("↓", CfgResources.DOWN,JLabel.CENTER),
		new JLabel("←", CfgResources.LEFT, JLabel.CENTER),
		new JLabel("→", CfgResources.RIGHT,JLabel.CENTER)},
		//第二组方法说明标签
		{ new JLabel("暂停", CfgResources.EXTRA1, JLabel.CENTER),
		new JLabel("下落", CfgResources.EXTRA2, JLabel.CENTER),
		new JLabel("阴影", CfgResources.EXTRA3, JLabel.CENTER),
		new JLabel("4", CfgResources.EXTRA4, JLabel.CENTER) } };
	private ArrayList<SetField> ctrlFields = null;
	private ArrayList<SetField> extraFields = null;
	/*
	 * 存放两组方法名，对应游戏接口中的控制操作和特殊操作
	 */
	private static final String[][] METHOD_NAME = {
		{"keyUp", "keyDown", "keyLeft", "keyRight"},
		{"keyExtra1", "keyExtra2", "keyExtra3", "keyExtra4"}
	};
	
	private static File setFile = new File(CfgResources.FILE);
	public SettingPanel() {
	    super();
	    
    }

	public SettingPanel( LayoutManager layout) {
	    super(layout);
	    //初始化按键设置
	    this.initKeySetting();
	    JPanel ctrlPane = titlePane("控制按钮", 0);
		ctrlPane.setBounds(10, 10, 180, 200);
		this.add(ctrlPane);
		JPanel extraPane = titlePane("额外功能", 1);
		extraPane.setBounds(250, 10, 180, 200);
		this.add(extraPane);
    }
	/**
	 * 为预定义的两组方法名设置对应键位
	 * 返回一个包含标题的设置面板，里面有标签和按键设置框
	 * @param title 面板说明标题
	 * @param group 为0返回控制操作设置面板，为1返回特殊操作设置面板
	 * @return 返回一个包含标题的设置面板
	 */
	public JPanel titlePane(String title, int group) {
		ArrayList<SetField> fields;
		if (group == 0) {
			fields = ctrlFields;
		}else if (group == 1) {
			fields = extraFields;
		}else
			return null;
		
		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createTitledBorder(title));
		pan.setLayout(new GridLayout(4, 2, 0, 2));
		
		for (int i = 0; i < 4; i++ ) {
			pan.add(labels[group][i]);
			pan.add(fields.get(i));
		}
		return pan;
	}
	void initKeySetting() {
		ArrayList<SetField> fields = null;
		ctrlFields = new ArrayList<SetField>(4);
		extraFields = new ArrayList<SetField>(4);
		//如果设置文件存在，读取设置文件，否则采用预设值
		if (setFile.exists()) {
			try {
	            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(setFile));
	            @SuppressWarnings( "unchecked")
                HashMap<String, Integer> settings = (HashMap<String, Integer>)ois.readObject();
	            ois.close();
	            //遍历方法名
				for (int x = 0; x < 2; x++) {
					if ( x == 0 ) 
						fields = ctrlFields;
					else if (x == 1)
						fields = extraFields;
					for (int y = 0; y < 4; y++) {
						String methodName = METHOD_NAME[x][y];
						// 若哈希表中存在该方法名，设置区域中加入对应按键号码
						if (settings.containsKey(methodName))
							fields.add(new SetField(settings.get(methodName), methodName));
					}
				}
            } catch ( ClassNotFoundException | IOException e) {
	            e.printStackTrace();
            }
		}else {
		//预定义按钮
		ctrlFields.add(new SetField(KeyEvent.VK_UP, METHOD_NAME[0][0]));
		ctrlFields.add(new SetField(KeyEvent.VK_DOWN, METHOD_NAME[0][1]));
		ctrlFields.add(new SetField(KeyEvent.VK_LEFT, METHOD_NAME[0][2]));
		ctrlFields.add(new SetField(KeyEvent.VK_RIGHT, METHOD_NAME[0][3]));
		extraFields.add(new SetField(KeyEvent.VK_P, METHOD_NAME[1][0]));
		extraFields.add(new SetField(KeyEvent.VK_SPACE, METHOD_NAME[1][1]));
		extraFields.add(new SetField(KeyEvent.VK_S, METHOD_NAME[1][2]));
		extraFields.add(new SetField(KeyEvent.VK_Q, METHOD_NAME[1][3]));
		}
		fields = null;
	}

	public ArrayList<SetField> getCtrlFields() {
		return ctrlFields;
	}

	public ArrayList<SetField> getExtraFields() {
		return extraFields;
	}

	
}
