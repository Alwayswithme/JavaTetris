package ui;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import config.GameConfig;
import dto.Player;

public abstract class LayerData extends Layer {
	private static final int MAX_ROW = GameConfig.getDATA().getMaxRow();
	/**
	 * 间距
	 */
	private static int SPA;
	/**
	 * 相对起始Y坐标
	 */
	private static int START_Y;
	/**
	 * 值槽外径
	 */
	private static  final int EXP_H = RECT_H + 4;

	public LayerData(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		// 值槽上下间距
		SPA = (layerHeight - (GameImg.DB.getHeight(null) + (PADDING<<1) + (EXP_H)*5))/MAX_ROW;
		START_Y = PADDING + GameImg.DB.getHeight(null) + SPA;
	}
	/**
	 * 绘制该窗口所有值槽
	 * @param imgTitle 标题图片
	 * @param keeper   记录保持者
	 * @param g        画笔
	 */
	public void showData(Image imgTitle, List<Player> keepers, Graphics g) {
		//绘制标题图片
		g.drawImage(imgTitle, TITLE_X, TITLE_Y, null);
		//获取目前玩家分数
		int nowPoint = gameDto.getStatus().getScore();
		for (int i = 0 ; i < MAX_ROW; i++ ) {
			//获取一位记录保持者信息
			Player p = keepers.get(i);
			//获得记录分数
			int record = p.getScore();
			//计算目前玩家分数和记录分数的比值
			double rate = (double)nowPoint/record;
			//已破记录，，比值设为100%
			rate = rate > 1 ? 1.0 : rate;
			this.drawRect(locateY + START_Y + i*(EXP_H + SPA) , rate, g, p.getName(), Integer.toString(record));
		}
	}
	@Override
	abstract public void paint(Graphics g);

}
