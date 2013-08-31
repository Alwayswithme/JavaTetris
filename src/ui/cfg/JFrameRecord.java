package ui.cfg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import control.GameControl;

@SuppressWarnings( "serial")
public class JFrameRecord extends JFrame {
	private GameControl gameCtrl = null;
	
	private JButton okBut = null;
	
	private JLabel score = null;
	
	private JTextField txName = null;
	
	
	public JFrameRecord(GameControl gCtrl) {
		gameCtrl = gCtrl;
		this.setLayout(new BorderLayout());
		this.setTitle("保存记录");
		this.setSize(256, 150);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setPanel();
		this.setListener();		
	}
	public void show(int score) {
		this.score.setText("您的得分:" + score);
		this.setVisible(true);
	}
	// 确认按钮事件监听，判断输入合法后调用存储记录的方法
	private void setListener() {
		this.okBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = txName.getText();
				if (userName == null || "".equals(userName)) {
					JOptionPane.showMessageDialog(null,"用户名不能为空！" ,"请重新输入", JOptionPane.ERROR_MESSAGE);
				}else if(userName.length() > 16) {
					JOptionPane.showMessageDialog(null,"用户名过长，请输入16位以下字符！" ,"请重新输入", JOptionPane.ERROR_MESSAGE);
				}else {
					setVisible(false);
					gameCtrl.saveScore(userName);
				}
			
			}
		});
    }

	private void setPanel() {
		
		/*    北部面板     */
		JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//分数标签
		score = new JLabel();
		north.add(score);
		this.add(north, BorderLayout.NORTH);
		
		/*    中部面板     */
		JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//文本框
		txName = new JTextField("匿名", 10);
		
		txName.setSize(50, 20);
		center.add(new JLabel("您的名字"));
		center.add(txName);
		this.add(center, BorderLayout.CENTER);
		
		/*    南部面板     */
		//确定按钮
		okBut = new JButton("OK");
		//应用流式布局
		JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		south.add(okBut);
		//添加到主面板
		this.add(south, BorderLayout.SOUTH);
	}
	public void relativeTo(Component c) {
		this.setLocationRelativeTo(c);
    }
}
