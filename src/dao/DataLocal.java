package dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import dto.Player;

public class DataLocal implements Data {
	private final String savFile;
	
	public DataLocal(HashMap<String, String> paraMap) {
		savFile = paraMap.get("path");
	}
	
	@SuppressWarnings( "unchecked")
    @Override
	public List<Player> loadData(){
		ObjectInputStream ois = null;
		List<Player> players = null;
		File f = new File(savFile);
		if (!f.exists()) {
			//文件不存在，创建并返回空记录列表
			try {
	            f.createNewFile();
            } catch (IOException e) {
	            e.printStackTrace();
            }
			return null;
		}
		try {
	        ois = new ObjectInputStream(new FileInputStream(f));
	        players = (List<Player>) ois.readObject();
        } catch (FileNotFoundException e) {
	        e.printStackTrace();
        } catch (ClassNotFoundException e) {
	        e.printStackTrace();
        } catch (EOFException e) {
			System.out.println(e.getMessage());
			createObjStream();
		}catch (IOException e) {
        	e.printStackTrace();
        }finally {
        	try {
        		if (ois != null)
        			ois.close();
            } catch (IOException e) {
	            e.printStackTrace();
            }
        }
		return players;
	}
	private void createObjStream() {
		List<Player> players = new ArrayList<>(5);
		ObjectOutputStream oos = null;
		try {
	        oos = new ObjectOutputStream(new FileOutputStream(savFile));
	        oos.writeObject(players);
		} catch (FileNotFoundException e) {
	        e.printStackTrace();
        } catch (IOException e) {
	        e.printStackTrace();
        }finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }

	}

	@Override
	public void saveData(Player player) {
		List<Player> players = this.loadData();
		if (players == null ) {
			players = new ArrayList<>(5);
		}
		newRecord(player, players);
		ObjectOutputStream oos = null;
		try {
	        oos = new ObjectOutputStream(new FileOutputStream(savFile));
	        oos.writeObject(players);
		} catch (FileNotFoundException e) {
	        e.printStackTrace();
        } catch (IOException e) {
	        e.printStackTrace();
        }finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	/*
	 * create a new record list
	 */
	public static void newRecord(Player player, List<Player> players) {	    
		if (players.size() >= 5) {
			//排序后比较，如果大于第五名则取代他
			Collections.sort(players);
			if (player.getScore() > players.get(4).getScore()) {
				players.set(4, player);
			}
		}else {
			players.add(player);
		}
		Collections.sort(players);
    }

}
