package plugin.spigot.defaulpackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;



public class FileManager {

	//Append the content string to the end of file specified in filePath
	public static void AppendStringOnFile(String filePath, String content) {
		StringBuilder sb = new StringBuilder();
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
        	String line;
            
            while ((line = br.readLine()) != null) {
        		sb.append(line).append(System.lineSeparator());
            }

        } catch (IOException ex) {
        	System.err.format("IOException: %s%n", ex);
        }
        
        sb.append(content).append(System.lineSeparator());
		
        // create again the file with the correct list
        try (FileWriter writer = new FileWriter(filePath); BufferedWriter l_BufferedWriter = new BufferedWriter(writer)) {
			 l_BufferedWriter.write(sb.toString());

		} catch (IOException ex) {
	            System.err.format("IOException: %s%n", ex);
		}
	}
	
	//Append the content string to the end of file specified in filePath
	public static void ReplaceStringOnFile(String filePath, String toReplace, String replaceWith) {
		StringBuilder l_ReaderStringBuilder = new StringBuilder();
    	String l_FileContent = "";
		
		// read online player file and instantiate the new one
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
        	String l_Line;
            
            while ((l_Line = br.readLine()) != null) {
            	l_ReaderStringBuilder.append(l_Line).append(System.lineSeparator());
            }
            
            l_FileContent = l_ReaderStringBuilder.toString();
            l_FileContent = l_FileContent.replace(toReplace, replaceWith);

        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
        
        // create again the file with the correct list
		try (FileWriter writer = new FileWriter(filePath); BufferedWriter l_BufferedWriter = new BufferedWriter(writer)) {
			l_BufferedWriter.write(l_FileContent);
		
		} catch (IOException ex) {
			System.err.format("IOException: %s%n", ex);
	 	}
	}
	
	
	@Deprecated
	public static boolean writeCoordOnFile(CustomLocation cl) {
		
        File f = new File("coordinateTEST.txt");
        ArrayList<CustomLocation> cls = new ArrayList<>();
        cls = readCoordsFromFile();
        cls.add(cl);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cls);
            oos.close();
            fos.close();
            return true;
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
	}
	
	public static List<CustomLocation> readAllCSVCoord() {
		List<CustomLocation> cls = new ArrayList<CustomLocation>();
		try (
				Reader reader = Files.newBufferedReader(Paths.get(ConfigProperties.COORDS_FILE.getValue()));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
						.withHeader("NAME", "X", "Y", "Z", "HIDDEN")
						.withFirstRecordAsHeader()
						.withIgnoreHeaderCase()
						.withTrim());
				) {
			for (CSVRecord csvRecord : csvParser) {
				cls.add(new CustomLocation(
						csvRecord.get("NAME"), 
						Integer.parseInt(csvRecord.get("X")), 
						Integer.parseInt(csvRecord.get("Y")), 
						Integer.parseInt(csvRecord.get("Z")),
						csvRecord.get("HIDDEN").equalsIgnoreCase("true") ? true : false
						));
			}
		} catch (IOException e) {
			System.out.print("Problemi nella lettura del file");
		}
		return cls;
	}

	public static boolean saveCSVCoord(CustomLocation cl){
		List<CustomLocation> cls = readAllCSVCoord();
		cls.add(cl);	
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ConfigProperties.COORDS_FILE.getValue()));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
						.withHeader("NAME", "X", "Y", "Z", "HIDDEN")
						);
				) {
			for(CustomLocation singleCl : cls) {
				csvPrinter.printRecord(
						singleCl.getName(), 
						singleCl.getX(), 
						singleCl.getY(), 
						singleCl.getZ(), 
						singleCl.isHidden());

			}
			csvPrinter.flush();         
			return true;
		} catch (IOException e) {
			System.out.print("Problemi nella scrittura del file");
			return false;
		}
	}

	
	
	@Deprecated
	public static boolean writeCoordOnFile(ArrayList<CustomLocation> cls) {
        File f = new File("coordinateTEST.txt");

        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cls);
            oos.close();
            fos.close();
            return true;
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
	}
	
	public static boolean removeCSVCoord(String locationName) {
		boolean found = false;
		ArrayList<CustomLocation> updatedSavedLocations = new ArrayList<CustomLocation>();
		for(CustomLocation cl : readAllCSVCoord()) {
			if (!cl.getName().equalsIgnoreCase(locationName)) {
				updatedSavedLocations.add(cl);
			} else {
				found = true;
			}
		}
		
		writeCoordOnFile(updatedSavedLocations);
		
		return found;
	}
	
	@Deprecated
	public static ArrayList<CustomLocation> readCoordsFromFile() {
		ArrayList<CustomLocation> cls = new ArrayList<>();
		 File f = new File("coordinateTEST.txt");
	        try {
	            FileInputStream fis = new FileInputStream(f);
	            ObjectInputStream ois = new ObjectInputStream(fis);
	            
	            cls = (ArrayList<CustomLocation>)ois.readObject();
	            ois.close();
	            fis.close();
	            
	        } catch (FileNotFoundException ex) {
	            ex.printStackTrace();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        } catch (ClassNotFoundException ex) {
	            ex.printStackTrace();
	        }
	        return cls;
	}
}
