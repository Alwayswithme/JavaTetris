package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameFunction {
	/**
	 * 下落线程睡眠时间
	 * @param level
	 * @return
	 */
	public static long getSleepTimeByLevel(int level) {
		long time = - (level * 70) + 800;
		return time <= 100 ? 100 : time;
	}
	/**
	 * 对象深克隆
	 * @param src
	 * @return
	 */
	
	// 用序列化与反序列化实现深克隆
	public Object deepClone(Object src) {
		Object o = null;
		try	{
			if (src != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(src);
				oos.close();
				ByteArrayInputStream bais = new ByteArrayInputStream(baos
				                .toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				o = ois.readObject();
				ois.close();
			}
		} catch (IOException e)	{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	}
    /**
     * 制造障碍线程间隔时间
     */
	public static int getIntervalMinute(int level) {
		int ms = 1000;
		int time = - (level * 5) + 80;
		return time <= 10 ? 10*ms : time*ms;
	}
}
