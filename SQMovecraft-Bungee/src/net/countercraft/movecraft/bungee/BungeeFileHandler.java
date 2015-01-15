package net.countercraft.movecraft.bungee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.countercraft.movecraft.Movecraft;

public class BungeeFileHandler {
	private static File folder = new File(Movecraft.getInstance().getDataFolder().getAbsoluteFile().
										  getParentFile().getParentFile().getParentFile().getParentFile() +
										  "/starships");
	
	public static void saveCraftBytes(byte[] craftdata, String pilot){
		File target = new File(folder + "/" + pilot + ".starship");
		if(target.exists()){
			target.delete();
		}
		try{
			target.createNewFile();
			FileOutputStream out = new FileOutputStream(target);
			out.write(craftdata);
			out.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	public static byte[] readCraftBytes(String pilot){
		File target = new File(folder + "/" + pilot + ".starship");
		if(!target.exists()){
			System.out.println("ERROR: File does not exist!: " + target.toString());
			return null;
		}
		byte[] inbytes = null;
		try{
			FileInputStream in = new FileInputStream(target);
			inbytes = new byte[in.available()];
			in.read(inbytes);
			in.close();
		} catch (Exception e){
			System.out.println("crash possibility 2");
			e.printStackTrace();
		}
		return inbytes;
	}
}
