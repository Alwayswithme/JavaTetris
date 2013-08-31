package ui.cfg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ui.GameImg;
import control.GameControl;

@SuppressWarnings( "serial")
public class JFrameConfig extends JFrame {
	
	public static final Font FONT_ONE = new Font("AR PL UMing CN", Font.PLAIN, 20);

	private GameControl gControl;
	
	private  JLabel jLabel = new JLabel();
	private static final SettingPanel setPanel = new SettingPanel(null);
	
	private static final SkinPanel skinPanel = new SkinPanel();
	private static JButton[] buttons = {
	                new JButton(CfgResources.OK),
	                new JButton(CfgResources.CANCEL),
	                new JButton(CfgResources.APPLY) };

	
	public JFrameConfig(String name, GameControl gControl) {
		// 设定标题图标，关闭行为
		super(name);
		this.setIconImage(CfgResources.CTRL_IMG);
		
		this.gControl = gControl;
		// 居中显示，设定大小不可调节
		this.setSize(460, 320);
		this.setResizable(false);
		// 设置布局管理器为边界布局
		this.setLayout(new BorderLayout());
		// 添加主面板
		this.add(mainPane(), BorderLayout.CENTER);
		// 添加按钮面板
		this.add(buttonPane(), BorderLayout.SOUTH);
		
		this.writeConfig();
	}

	public void relativeTo(Component c) {
		this.setLocationRelativeTo(c);
    }
	
	/**
	 * 主面板
	 */
	private JTabbedPane mainPane() {
		JTabbedPane jtp = new JTabbedPane();
		jtp.setFont(FONT_ONE);
		jtp.addTab("控制", CfgResources.CTRL, setPanel);
		jtp.addTab("皮肤", CfgResources.SKIN, skinPanel);
		return jtp;
	}
	
	/**
	 * 按钮面板
	 */
	private JPanel buttonPane() {
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
		this.jLabel.setFont(FONT_ONE);
		this.jLabel.setForeground(Color.RED);
		jp.add(jLabel);
		
		// 给确定按钮增加事件监听
		buttons[0].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (writeConfig()) {
					setVisible(false);
					gControl.setOver();
				}
				
			}
		});
		// 给取消窗口增加事件监听
		buttons[1].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				gControl.setOver();
			}
		});
		// 给应用按钮增加事件监听
		buttons[2].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 保存配置
				writeConfig();
				
				// 重新调整键盘按键
				gControl.setKeyBorad();
				gControl.setOver();
				requestFocus();
			}
		});
		for (JButton but : buttons) {
			jp.add(but);
		}
		jp.add(new JLabel());
		return jp;
	}
	
	/**
	 * 保存游戏配置
	 * 
	 * @return
	 */
	private boolean writeConfig() {
		HashMap<String, Integer> settings = new HashMap<String, Integer>();
		// 获取控制操作设置，建立映射关系
		addToMap(settings, setPanel.getCtrlFields());
		// 获取特殊操作设置，建立映射关系
		addToMap(settings, setPanel.getExtraFields());
		if (settings.size() != 8) {
			jLabel.setText("setting did not save!!");
			jLabel.setIcon(CfgResources.CANCEL);
			return false;
		}
		// 切换皮肤
		//获得选中的皮肤名
		String skinName = skinPanel.getSkinList().getSelectedValue();
		//初始化皮肤素材
		GameImg.setSkin(skinName+File.separator);
		
		// 键盘配置用对象流写入配置文件
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(CfgResources.FILE));
			oos.writeObject(settings);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			jLabel.setText(e.getMessage());
			jLabel.setIcon(CfgResources.CANCEL);
			return false;
		}
		//写入配置
		jLabel.setText("save successed");
		jLabel.setIcon(CfgResources.OK);
		return true;
	}
	
	public void addToMap(HashMap<String, Integer> settings, List<SetField> temp) {
		// 遍历所有设定区域
		for (SetField sf : temp) {
			// 取得每个设定区域所存储的方法名，键盘按键码
			String method = sf.getMethodName();
			int keyCode = sf.getKeyCode();
			if (keyCode == 0)
				return;
			// 将方法名和键盘对应按键，存入哈希表
			settings.put(method, keyCode);
		}
	}
	
	public JLabel getJLabel() {
		return this.jLabel;
	}

}
