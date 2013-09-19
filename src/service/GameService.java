package service;

/*
 * 游戏按键事件
 */
public interface GameService {
	
	boolean keyUp();
	
	boolean keyDown();
	
	boolean keyLeft();
	
	boolean keyRight();
	
	void keyExtra1();
	
	void keyExtra2();
	
	void keyExtra3();
	
	void keyExtra4();
	
	void gameRun();
	
	void mainAction();
}
