package plugin.spigot.defaulpackage;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import static net.md_5.bungee.api.ChatColor.*;

public enum MSG {
	
	PLAYER_JOIN(new String[] {"[p] ha acceduto", "[p] è entrato", "Ecco [p] u trmon"}),
	PLAYER_LEFT(new String[] {"[p] è scappato via", "[p] è uscito", "[p] è andato"}),
	SLEEP(new String[] {"[p] sta dormendo", "[p] sà mìs a dorm", "[p] è andato a dormire", "[p] è andato a letto", "[p] dorme", "[p] sta già russando"}),
	GET_DAMAGE(new String[] {"[p] ti ha inchiappettato", "[p] ti ha dato una botta", "[p] ti sta menando", "[p] ti ha fatto la bua", "[p] ha voglia di te"}),
	WAKE_UP(new String[] {"[p] bastardo si è alzato", "[p] si è svegliato"}),
	GOOD_MORNING(new String[] {"Buongiorno trimone", "Buongiorno, ben svegliato", "Buongiorno un cazzo"}),
	ENCHANTMENT(new String[] {"Il bastardo [p] ha incantato un oggetto", "[p] è un bastardo perchè ha incantato un oggetto", "Complimenti al bastardo [p] per aver incantato un oggetto"}),
	IN_NETHER(new String[] {"[p] è entrato nel " + DARK_RED + "Nether"}),
	IN_OVERWORLD(new String[] {"[p] è tornato nell'" + DARK_GREEN + "OverWorld"});

	private String[] phrases;

	private MSG(String... phrases)
	{
		//this.nicknames = Arrays.stream(nicknames).map(s -> s.replace("[p]", "ok")).toArray(size -> new String[size]);
		this.phrases = Arrays.stream(phrases).map(s -> GRAY + s).toArray(size -> new String[size]);
	}


	public String getMessage() {
		return phrases[new Random().nextInt(phrases.length)];
	}

	public String getMessage(Player player) {
		return phrases[new Random().nextInt(phrases.length)].replace("[p]", GOLD + player.getDisplayName() + GRAY);
	}
}



