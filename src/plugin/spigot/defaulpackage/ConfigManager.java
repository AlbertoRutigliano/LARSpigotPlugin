package plugin.spigot.defaulpackage;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
 
public class ConfigManager {
	private File vCustomConfigFile;
    private FileConfiguration vCustomConfig;
 
	public ConfigManager()
	{
		this.CreateCustomConfig();
	}
	
	//Create config file if doesn't exist and populate it with base configuration
	private void CreateCustomConfig() {
        this.vCustomConfigFile = new File("./plugins/"+Main.getPlugin(Main.class).getName()+"/config.yml");
        
        if (!vCustomConfigFile.exists()) {
            vCustomConfigFile.getParentFile().mkdirs();
            try {
				this.vCustomConfigFile.createNewFile();
				
				//Default values
				FileManager.AppendStringOnFile(this.vCustomConfigFile.getPath(), "serverTestPort: 0");
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        this.vCustomConfig= new YamlConfiguration();
        try {
        	this.vCustomConfig.load(vCustomConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
        	e.printStackTrace();
        }
    }
	
	public FileConfiguration GetCustomConfig()
	{
        return this.vCustomConfig;
    }
}