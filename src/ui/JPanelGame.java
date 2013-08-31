package ui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import config.ButtonConfig;
import config.FrameConfig;
import config.GameConfig;
import config.LayerConfig;
import control.GameControl;
import dto.GameDto;

public class JPanelGame extends JPanel {
	
	/**
	 * 
	 */
    private static final long serialVersionUID = -2038722807878199968L;
	private JButton start;
	private JButton setting;
	private ArrayList<Layer> layers = null;

	private GameControl ctrler = null;
	
	public JPanelGame(GameControl gControl, GameDto dto) {
		// Frame元素配置
		FrameConfig fCfg = GameConfig.getFRAME();
		ctrler = gControl;
		// 给面板增加键盘事件监听
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ctrler.actionByKeyCode(e.getKeyCode());
			}
		});
		this.setLayout(null);

		// 初始化层
		initLayer(fCfg, dto);
		
		// 初始化组件
		initComponent(fCfg, dto);
	}
	
	/**
	 * 初始化组件及监听各类事件
	 * @param FrameConfig 游戏信息配置对象
	 * @param GameDto     游戏数据源
	 */
	private void initComponent(FrameConfig fCfg, GameDto dto) {
		
		ButtonConfig bConfig = fCfg.getButtonConfig();
		this.start = new JButton(GameImg.START);
		start.setBounds(bConfig.getStartX(), bConfig.getStartY(), bConfig.getWidth(),
		                bConfig.getHeight());
		this.add(start);
		// 给开始按钮增加事件监听
		this.start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrler.start();
			}
		});
		
		this.setting = new JButton(GameImg.SETTING);
		setting.setBounds(bConfig.getSettingX(), bConfig.getSettingY(), bConfig.getWidth(),
		                bConfig.getHeight());
		this.add(setting);
		// 给设置按钮增加事件监听
		this.setting.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrler.setting();
			}
		});
		
	}
	
	/**
	 * 初始化窗口和游戏布局层
	 * @param FrameConfig  内部窗口配置信息对象
	 * @param GameDto      游戏数据源
	 */
	private void initLayer(FrameConfig fCfg, GameDto dto) {
		try {
			
			// frame嵌套元素layer配置，获取多个层配置信息
			List<LayerConfig> layersCfg = fCfg.getLayersConfig();
			
			// 创建游戏层数组
			layers = new ArrayList<Layer>(layersCfg.size());
			
			// 遍历每一个层配置信息
			for (LayerConfig layCfg : layersCfg) {
				// 由层配置信息对象得到全限定名和各种参数，并应用反射创建层对象
				Class<?> layerClass = Class.forName(layCfg.getClassName());
				Constructor<?> cons = layerClass.getConstructor(int.class, int.class,
				                int.class, int.class);
				Layer aLayer = (Layer) cons.newInstance(
				                layCfg.getX(), layCfg.getY(), layCfg.getW(), layCfg.getH());
				aLayer.setDto(dto);
				// 层对象加入数组
				layers.add(aLayer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 按钮是否可点击
	 */
	public void buttonSwitch(boolean bool) {
		start.setEnabled(bool);
		setting.setEnabled(bool);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		// 调用基类方法
		super.paintComponent(g);
		// 绘制游戏画面
		for (Layer layer : layers) {
			layer.paint(g);
		}
		start.setIcon(GameImg.START);
		setting.setIcon(GameImg.SETTING);
		// 面板获得焦点
		this.requestFocus();
	}

}
