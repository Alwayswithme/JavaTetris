package ui.cfg;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

@SuppressWarnings( "serial")
public class SetField extends JTextField {
	
	private int keyCode;
	private String methodName;
	protected static final Font FONT_ONE = new Font(Font.DIALOG, Font.PLAIN, 14);
	public SetField(int keyCode, String methodName) {
		setHorizontalAlignment(JTextField.CENTER);
		this.keyCode = keyCode;
		this.methodName = methodName;
		this.setText(KeyEvent.getKeyText(keyCode));
		this.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int i = e.getKeyCode();
				setKeyCode(i);
				String keyText = KeyEvent.getKeyText(i);
				setText(keyText);
			}
		});
		setFont(FONT_ONE);
	}
	public int getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public static Font getFontOne() {
		return FONT_ONE;
	}
}
