package plugin.spigot.defaultpackage;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;


import static net.md_5.bungee.api.ChatColor.*;

public enum MSG {
	PLAYER_JOIN (new String[] {"[p] ha acceduto", "[p] � entrato", "Ecco [p] u trmon", "Un caldo benvenuto a [p] u trmon", "Ha trasut [p]", "Awe, cretino [p]!"}),
	PLAYER_LEFT (new String[] {"[p] � scappato via", "[p] � uscito", "[p] � andato", "[p] aveva qualcosa di meglio da fare"}),
	SLEEP (new String[] {"[p] sta dormendo", "[p] s� m�s a dorm", "[p] � andato a dormire", "[p] � andato a letto", "[p] dorme", "[p] sta gi� russando", "[p] sta gi� contando le pecorelle"}),
	CANT_SLEEP (new String[] {"[p] non pu� dormire per colpa dei mostri"}),
	GET_DAMAGE (new String[] {"[p] ti ha inchiappettato", "[p] ti ha dato una botta", "[p] ti sta menando", "[p] ti ha fatto la bua", "[p] ha voglia di te", "[p] te le sta dando di santa ragione!"}),
	WAKE_UP (new String[] {"[p] bastardo si � alzato", "[p] si � svegliato di soprassalto", "[p] s'alzat", "[p] si � alzato per bere un bicchiere d'acqua"}),
	GOOD_MORNING (new String[] {"Buongiorno trimone", "Buongiorno cazzone", "Buongiornissimo, caff�?", "Buongiorno, ben svegliato", "Buongiorno un cazzo", "Buongiorno principessa!"}),
	ENCHANTMENT (new String[] {"Il bastardo [p] ha incantato un oggetto", "[p] � un bastardo perch� ha incantato un oggetto", "Complimenti al bastardo [p] per aver incantato un oggetto", "Formidabile bastardo [p] ha incantato un oggetto"}),
	IN_NETHER (new String[] {"[p] � entrato nel " + DARK_RED + "Nether"}),
	IN_OVERWORLD (new String[] {"[p] � tornato nell'" + DARK_GREEN + "OverWorld"}),
	QUOTE (new String[] {"Occhio non vede, cuore non duole", "La gatta frettolosa fece i gattini ciechi", "Una mela al giorno leva il medico di torno", "Non tutte le ciambelle escono con il buco", "L�occasione fa l�uomo ladro", "Se non � zuppa � pan bagnato", "Il vovo dice cornuto al ciuccio", "Campa cavallo che l�erba cresce", "A goccia a goccia si scava la roccia", "Can che abbaia non morde", "Non dire gatto se non ce l�hai nel sacco", "Oggi a me domani a te", "Il lupo perde il pelo ma non il vizio", "Chi si impiccia, resta impicciato", "Occhio per occhio, dente per dente", "Chi ha avuto ha avuto e chi ha dato ha dato", "Altezza mezza bellezza", "A volte dorme di pi� lo sveglio che il dormiente", "Chi dorme non piglia pesci", "Il gioco � bello quando dura poco", "Tra moglie e marito non mettere il dito", "Chi non risica non rosica", "Chiodo scaccia chiodo", "Chi va con lo zoppo impara a zoppicare", "Stip ca truv", "Campa cavallo che l'erba cresce", "A caval donato non si guarda in bocca", "Chi sa fare, sa capire", "L'erba del vicino � sempre pi� buona", "Morto un papa se ne fa un altro", "Non � tutto oro quel che luccica", "Anche l�occhio vuole la sua parte"});

	private String[] phrases;

	private MSG(String... phrases)
	{
		//this.nicknames = Arrays.stream(nicknames).map(s -> s.replace("[p]", "ok")).toArray(size -> new String[size]);
		this.phrases = Arrays.stream(phrases).map(s -> GRAY + s).toArray(size -> new String[size]);
	}

	private MSG(List<String> stringList) {
		phrases = stringList.toArray(new String[0]);
	}

	public String getMessage() {
		return phrases[new Random().nextInt(phrases.length)];
	}

	public String getMessage(Player player) {
		return phrases[new Random().nextInt(phrases.length)].replace("[p]", GOLD + player.getDisplayName() + GRAY);
	}
}



