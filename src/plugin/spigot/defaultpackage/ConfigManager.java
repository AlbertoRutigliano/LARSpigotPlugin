package plugin.spigot.defaultpackage;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
 
public class ConfigManager {
	private static File vCustomConfigFile;
    private static FileConfiguration vCustomConfig;
 
    
	//Create config file if doesn't exist and populate it with base configuration
	public static void CreateCustomConfig() {
        vCustomConfigFile = new File("./plugins/"+Main.getPlugin(Main.class).getName()+"/config.yml");
        
        if (!vCustomConfigFile.exists()) {
            vCustomConfigFile.getParentFile().mkdirs();
            try {
				vCustomConfigFile.createNewFile();
				
				//Default values
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.SERVER_TEST_PORT.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.SECONDS_TO_AFK.toString());
				FileManager.AppendStringOnFile(vCustomConfigFile.getPath(), ConfigProperties.COORDS_FILE.toString());
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
	
	public static FileConfiguration GetCustomConfig()
	{
        return vCustomConfig;
    }
}