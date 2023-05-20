package lar.spigot.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import lar.spigot.plugin.commands.CoordsCommand;
import lar.spigot.plugin.commands.JokeCommand;
import lar.spigot.plugin.commands.PlayerposCommand;
import lar.spigot.plugin.commands.ThanksCommand;
import lar.spigot.plugin.commands.TrackCommand;
import lar.spigot.plugin.managers.ConfigManager;
import lar.spigot.plugin.managers.MSGManager;
import lar.spigot.plugin.managers.PlayerManager;
import lar.spigot.plugin.managers.ServerManager;

import org.bukkit.event.Listener;

import static lar.spigot.plugin.commands.Commands.*;

import java.nio.file.Paths;

import org.bukkit.Server;

public class Main extends JavaPlugin implements Listener {
	public static Server MyServer;
	
	private PlayerManager vPlayerManager;

    private TrackRunner trackRunner;
	
	@Override
	public void onEnable() {
		ConfigManager.CreateCustomConfig();
		MyServer = getServer();
		
		this.vPlayerManager = new PlayerManager(this);
		
		MyServer.getPluginManager().registerEvents(this, this);
		MyServer.getPluginManager().registerEvents(vPlayerManager, this);

		
        this.trackRunner = new TrackRunner();
        this.trackRunner.runTaskTimer(this, 0, 5);
        
        this.getCommand(TRACK).setExecutor(new TrackCommand(this));
        
		this.getCommand(COORDS).setExecutor(new CoordsCommand());
		this.getCommand(COORDS).setTabCompleter(new CoordsCommand());
		
		this.getCommand(PLAYERPOS).setExecutor(new PlayerposCommand());
		
		this.getCommand(JOKE).setExecutor(new JokeCommand());

		this.getCommand(THANKS).setExecutor(new ThanksCommand());

		ServerManager.setTestServerPort(ConfigManager.GetCustomConfig().getInt(ConfigProperties.SERVER_TEST_PORT.name()));

		ServerManager.InitScoreboard();
		
		ServerManager.InitRandomQuote();
		
		ServerManager.InitSleepingKicker();
		
		MSGManager.loadMessagesFile(Paths.get(ConfigProperties.PLUGIN_FOLDER_PATH.getValue(), ConfigManager.GetCustomConfig().getString(ConfigProperties.MESSAGES_FILE.name())).toString());

	}
	
	@Override
	public void onDisable() {
		ServerManager.RemoveObjectives();
	}
	
    public TrackRunner getTrackRunner() {
        return trackRunner;
    }
	
}

