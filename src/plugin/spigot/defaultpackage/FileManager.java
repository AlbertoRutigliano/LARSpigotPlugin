package plugin.spigot.defaultpackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	public static List<CustomLocation> readAllCSVCoords() {
		List<CustomLocation> cls = new ArrayList<CustomLocation>();
		try (
				Reader reader = Files.newBufferedReader(Paths.get(ConfigProperties.PLUGIN_FOLDER_PATH.getValue() + ConfigManager.GetCustomConfig().getString(ConfigProperties.COORDS_FILE.name())));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
						.withHeader("NAME", "X", "Y", "Z", "HIDDEN", "WORLD_NAME")
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
						csvRecord.get("HIDDEN").equalsIgnoreCase("true") ? true : false,
						csvRecord.get("WORLD_NAME")
						));
			}

			// Sort in alphabetic order (by location Name field)
			Collections.sort(cls, new Comparator<CustomLocation>() {
				@Override
				public int compare(CustomLocation cl1, CustomLocation cl2) {
					return cl1.getName().compareToIgnoreCase(cl2.getName());
				}
			});
		} catch (IOException e) {
			System.out.print("Problemi nella lettura del file");
		}
		return cls;
	}

	public static boolean saveCSVCoord(CustomLocation cl){
		List<CustomLocation> cls = readAllCSVCoords();
		cls.add(cl);
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ConfigProperties.PLUGIN_FOLDER_PATH.getValue() + ConfigManager.GetCustomConfig().getString(ConfigProperties.COORDS_FILE.name())));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
						.withHeader("NAME", "X", "Y", "Z", "HIDDEN", "WORLD_NAME"));
				) {
			for(CustomLocation singleCl : cls) {
				csvPrinter.printRecord(
						singleCl.getName(), 
						singleCl.getX(), 
						singleCl.getY(), 
						singleCl.getZ(), 
						singleCl.isHidden(),
						singleCl.getWorldName());

			}
			csvPrinter.flush();         
			return true;
		} catch (IOException e) {
			System.out.print("Problemi nella scrittura del file");
			return false;
		}
	}

	public static boolean removeCSVCoord(String locationName) {
		boolean found = false;
		ArrayList<CustomLocation> updatedSavedLocations = new ArrayList<CustomLocation>();
		for(CustomLocation cl : readAllCSVCoords()) {
			if (!cl.getName().equalsIgnoreCase(locationName)) {
				updatedSavedLocations.add(cl);
			} else {
				found = true;
			}
		}		
		return found;
	}
}
