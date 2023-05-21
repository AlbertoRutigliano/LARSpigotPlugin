package lar.spigot.plugin.managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import lar.spigot.plugin.ConfigProperties;
 
public class ConfigManager {
	private static File vCustomConfigFile;
    private static FileConfiguration vCustomConfig;
    
    private static File messagesConfigFile;
    
	//Create config file if doesn't exist and populate it with base configuration
	public static void CreateCustomConfig() {

        vCustomConfigFile = new File(ConfigProperties.PLUGIN_FOLDER_PATH.getValue() + ConfigProperties.CONFIG_FILE.getValue());

        if (!vCustomConfigFile.exists()) {
            vCustomConfigFile.getParentFile().mkdirs();
            try {
				vCustomConfigFile.createNewFile();
				
				//Default values
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.SERVER_TEST_PORT.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.SECONDS_TO_AFK.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.COORDS_FILE.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.MESSAGES_FILE.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.MINUTES_QUOTE_INTERVAL.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.SECONDS_TO_NOT_SLEEPING_KICK.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.PERCENTAGE_SLEEPING_TO_NOT_SLEEPING_KICK.toString());
				
            } catch (IOException e) {
				e.printStackTrace();
			}
        }

        vCustomConfig= new YamlConfiguration();
        try {
        	vCustomConfig.load(vCustomConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
        	e.printStackTrace();
        }
    }
	
	public static FileConfiguration GetCustomConfig() {
        return vCustomConfig;
    }
	
	//Create messages.yml file if doesn't exist and populate it with default messages (from resources/messages.yml)
	public static void CreateDefaultMessagesConfig() {
		messagesConfigFile = new File(ConfigProperties.PLUGIN_FOLDER_PATH.getValue() + ConfigProperties.MESSAGES_FILE.getValue());
		 if (!messagesConfigFile.exists()) {
			 messagesConfigFile.getParentFile().mkdirs();
			 try {
				 InputStream targetStream = ConfigManager.class.getResourceAsStream("/messages.yml");
				 FileUtils.copyInputStreamToFile(targetStream, messagesConfigFile);
			 } catch (IOException e1) {
				 e1.printStackTrace();
			 }
		 }
	}
}