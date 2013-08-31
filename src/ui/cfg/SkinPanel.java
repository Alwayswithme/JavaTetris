package ui.cfg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ui.GameImg;

@SuppressWarnings( "serial")
public class SkinPanel extends JPanel {
	//列表栏
	private JList<String> skinList = null;
	
	//列表栏数据模型
	private DefaultListModel<String>  skinData = new DefaultListModel<String>();
	
	
	
	//预览图数组
	private static Image[] skinViewList = null;
	//预览面板
	private JPanel skinView = null;
	
	
	public SkinPanel() {
		this.setLayout(new BorderLayout(3, 5));
		File dir = new File(GameImg.GRAPHICS_PATH);
		File[] files = dir.listFiles();
		Arrays.sort(files);
		skinViewList = new Image[files.length];
		for (int i = 0, len = files.length; i < len; i++) {
			//增加选项
			skinData.addElement(files[i].getName());
			//增加预览图
			skinViewList[i] = new ImageIcon(files[i].getPath() + File.separator +"view.jpg").getImage();
		}
		//添加列表数据
		skinList = new JList<String>(skinData);
		//默认选中第一项
		skinList.setSelectedIndex(0);
		//监听
		skinList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				repaint();
			}
		
		
		});
		skinView = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(skinViewList[skinList.getSelectedIndex()], 0, 0, null);
			}
		};
		//
		JScrollPane scrollSkin = new JScrollPane(skinList);
		this.add(scrollSkin, BorderLayout.WEST); 
		scrollSkin.setPreferredSize(new Dimension(80, 300));
		this.add(skinView, BorderLayout.CENTER);
		this.setVisible(true);
	}


	public JList<String> getSkinList() {
		return skinList;
	}

}
