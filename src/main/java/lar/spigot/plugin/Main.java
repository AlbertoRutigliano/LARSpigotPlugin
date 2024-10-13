package lar.spigot.plugin;

import static lar.spigot.plugin.commands.Commands.COORDS;
import static lar.spigot.plugin.commands.Commands.FIGHT;
import static lar.spigot.plugin.commands.Commands.JOKE;
import static lar.spigot.plugin.commands.Commands.PLAYERPOS;
import static lar.spigot.plugin.commands.Commands.THANKS;
import static lar.spigot.plugin.commands.Commands.TRACK;

import java.nio.file.Paths;

import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lar.spigot.plugin.commands.CoordsCommand;
import lar.spigot.plugin.commands.FightCommand;
import lar.spigot.plugin.commands.JokeCommand;
import lar.spigot.plugin.commands.PlayerposCommand;
import lar.spigot.plugin.commands.ThanksCommand;
import lar.spigot.plugin.commands.TrackCommand;
import lar.spigot.plugin.managers.ConfigManager;
import lar.spigot.plugin.managers.MSGManager;
import lar.spigot.plugin.managers.PlayerManager;
import lar.spigot.plugin.managers.ServerManager;

public class Main extends JavaPlugin implements Listener {
	public static Server MyServer;

	private PlayerManager vPlayerManager;

	public void onLoad() {
		MyServer = getServer();
	}

	@Override
	public void onEnable() {

		ConfigManager.CreateCustomConfig();
		ConfigManager.CreateDefaultMessagesConfig();

		MyServer = getServer();

		this.vPlayerManager = new PlayerManager();

		MyServer.getPluginManager().registerEvents(this, this);
		MyServer.getPluginManager().registerEvents(vPlayerManager, this);

		this.getCommand(TRACK).setExecutor(new TrackCommand());

		this.getCommand(COORDS).setExecutor(new CoordsCommand());
		this.getCommand(COORDS).setTabCompleter(new CoordsCommand());

		this.getCommand(PLAYERPOS).setExecutor(new PlayerposCommand());

		this.getCommand(JOKE).setExecutor(new JokeCommand());

		this.getCommand(THANKS).setExecutor(new ThanksCommand());

		this.getCommand(FIGHT).setExecutor(new FightCommand());

		ServerManager
				.setTestServerPort(ConfigManager.GetCustomConfig().getInt(ConfigProperties.SERVER_TEST_PORT.name()));

		ServerManager.InitScoreboard();

		ServerManager.InitRandomQuote();

		ServerManager.InitSleepingKicker();

		MSGManager
				.loadMessagesFile(Paths
						.get(ConfigProperties.PLUGIN_FOLDER_PATH.getValue(),
								ConfigManager.GetCustomConfig().getString(ConfigProperties.MESSAGES_FILE.name()))
						.toString());

	}

	@Override
	public void onDisable() {
		ServerManager.RemoveObjectives();
	}

}
