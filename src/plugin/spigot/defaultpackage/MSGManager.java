package plugin.spigot.defaultpackage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MSGManager {
	
	public enum Message
	{
		PLAYER_JOIN,
		PLAYER_LEFT,
		SLEEP,
		CANT_SLEEP,
		GET_DAMAGE,
		WAKE_UP,
		GOOD_MORNING,
		ENCHANTMENT,
		IN_NETHER,
		IN_OVERWORLD,
		GO_TO_SLEEP,
		NOT_SLEEPING_KICK,
		QUOTE
	}
	
	private static HashMap<MSGManager.Message, List<String>> messages;

	public static void loadMessagesFile(String filePath)
	{
		if (messages == null) {
			messages = new HashMap<>();
		}
		
		FileConfiguration msgFileConfiguration = new YamlConfiguration();
        try {
    		msgFileConfiguration.load(filePath);
        } catch (IOException | InvalidConfigurationException e) {
        	e.printStackTrace();
        }
        
        for (MSGManager.Message message : MSGManager.Message.values()) {
        	List<String> readingMessages = msgFileConfiguration.getStringList(message.name());
        	messages.put(message, readingMessages);
		}
	}
	
	public static String getMessage(MSGManager.Message message)
	{
		return messages.get(message).get(new Random().nextInt(messages.get(message).size()));
	}

	public static String getMessage(MSGManager.Message message, Object... params)
	{
		return String.format(getMessage(message), params);
	}
	
}
