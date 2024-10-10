package lar.spigot.plugin.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

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
		OTHER_PLAYERS_NOT_SLEEPING_KICK,
		NOT_SLEEPING_KICK,
		FIGHT_DECLARATION,
		FIGHT_REQUEST_SEND,
		FIGHT_ACCEPT,
		FIGHT_ACCEPTED,
		FIGHT_DECLINE,
		FIGHT_DECLINED,
		FIGHT_LOOSE,
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
		return ChatColor.GRAY + messages.get(message).get(new Random().nextInt(messages.get(message).size()));
	}

	public static String getMessage(MSGManager.Message message, Object... params)
	{
		return ChatColor.GRAY + String.format(getMessage(message), params);
	}
	
}
