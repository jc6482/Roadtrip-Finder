package ranking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import ranking.rawLocation;

public class diskCaching {
	private final static String dir = "./src/main/resources/";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		storeLocationsInCache(null,null);
	}

	// Returns null if file not found
	public static HashSet<rawLocation> getCacheFromFile(Route theRoute){
		try {
			HashSet<rawLocation> retSet;
			String filename = dir + theRoute.generateFileName();
			FileInputStream file = new FileInputStream(filename);
			if(file == null)
				return null;
			ObjectInputStream in = new ObjectInputStream(file);
			retSet = (HashSet<rawLocation>) in.readObject();
			in.close();
			file.close();
			System.out.println("Deserialized HashMap");
			return retSet;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return null;
		}
	}
	
	public static void storeLocationsInCache(Route key,HashSet<rawLocation> values){
		try {
			String filename = dir + key.generateFileName();
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject((HashSet<rawLocation>)values);
			oos.close();
			fos.close();
			System.out.println("Serialized Locations added to " + filename);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
