package plugin.spigot.defaulpackage;

import static plugin.spigot.defaulpackage.Commands.JOKE;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class JokeCommand implements TabExecutor {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		// TODO Rimuovere se stesso
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(sender instanceof Player && JOKE.equalsIgnoreCase(label) ) {
			switch (args.length) {
			case 0:	
				ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED);
				sender.sendMessage(ChatColor.GRAY + "Scherzetto in corso...");
				break;
			case 1:	
				Player l_SelectedPlayer = Main.MyServer.getPlayer(args[0]);
				ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED, l_SelectedPlayer);
				sender.sendMessage(ChatColor.GRAY + "Scherzetto a " + l_SelectedPlayer.getDisplayName() + " in corso...");
				break;
			default: break;
			}

		}

		
		return true;
	}

}
