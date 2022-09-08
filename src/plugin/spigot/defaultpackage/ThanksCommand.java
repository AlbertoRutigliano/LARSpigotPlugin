package plugin.spigot.defaultpackage;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import static plugin.spigot.defaultpackage.Commands.*;

import java.util.List;



public class ThanksCommand  implements TabExecutor {
	
	public final static String[] THANKS_WORDS = new String[] {THANKS, GRAZIE, GRZ, TY, "tkx", "tnks", "ths", "thank", "<3", "ringrazio"};
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
	        Player p = (Player) sender;
	        String l_Command = label.toLowerCase();
			if(l_Command.equalsIgnoreCase(THANKS) || l_Command.equalsIgnoreCase(GRAZIE) || l_Command.equalsIgnoreCase(GRZ) || l_Command.equalsIgnoreCase(TY)) {
				makeHeartEffect(p);
			}
        }
        return true;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void makeHeartEffect(Player player) {
		Location playerLocation = player.getLocation();
        for (int degree = 0; degree < 360; degree+=60) {
            double radians = Math.toRadians(degree);
            double x = Math.cos(radians);
            double z = Math.sin(radians);
            Location heartLocation = playerLocation;
            heartLocation.add(x, 1.5, z);
            player.spawnParticle(Particle.HEART, heartLocation, 2);
            heartLocation.subtract(x, 1.5, z);
        }
	}
    
}


