package ui;
import javax.swing.JFrame;
import config.FrameConfig;
import config.GameConfig;
/**
 * V:view, 主窗口，镶嵌主界面，由控制器创建
 */
public class JFrameGame extends JFrame {

	/**
	 * 
	 */
    private static final long serialVersionUID = -4056290386335708982L;

	public JFrameGame(JPanelGame gamePanel) {
		//获得Frame配置信息
		FrameConfig fCfg = GameConfig.getFRAME();
		
		this.setTitle(fCfg.getWindowTitle());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setIconImage(GameImg.MAIN_ICON);
		
		this.setResizable(false);
		this.setSize(fCfg.getWidth(), fCfg.getHeight() );

		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Dimension screen = toolkit.getScreenSize();
		//System.out.println(screen.width +""+ screen.height);
		//int w = screen.width - this.getWidth() >>1;
		//int h = (screen.height - this.getHeight() >>1)-cfg.getWindowUp();
		//this.setLocation(200,300);
		this.setLocationRelativeTo(null);
		//default panel
		this.setContentPane(gamePanel);

		this.setVisible(true);
	}
}
