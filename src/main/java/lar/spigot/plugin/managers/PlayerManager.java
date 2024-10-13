package lar.spigot.plugin.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Note.Tone;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import lar.spigot.plugin.ConfigProperties;
import lar.spigot.plugin.ItemStackComparator;
import lar.spigot.plugin.Main;
import lar.spigot.plugin.commands.ThanksCommand;
import lar.spigot.plugin.entities.PlayerProperties;
import lar.spigot.plugin.entities.SortingType;
import lar.spigot.plugin.managers.MSGManager.Message;

import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;
/*
 * Event Handlers list: https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/event
 */
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerManager implements Listener {
	
    public static HashMap<Player, PlayerProperties> vPlayerProperties;
    
    private final Main plugin;

	public PlayerManager(Main plugin) {
		this.plugin = plugin;
		
		vPlayerProperties = new HashMap<Player, PlayerProperties>();
		for(Player p:Main.MyServer.getOnlinePlayers()) {
			vPlayerProperties.put(p, new PlayerProperties());
		}
		
		Main.MyServer.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class),  new Runnable() {
			public void run() {
				for (Player player : Main.MyServer.getOnlinePlayers()) {
					Timestamp now = new Timestamp(new Date().getTime());
					PlayerProperties l_CurrentPlayer = vPlayerProperties.get(player);
					if(l_CurrentPlayer != null) {
						int seconds = (int) ((now.getTime() - l_CurrentPlayer.getLastMoveTimestamp().getTime()) / 1000) % 60 ;
						if (seconds > ConfigManager.GetCustomConfig().getInt(ConfigProperties.SECONDS_TO_AFK.name())) {
							l_CurrentPlayer.setAfk(true);
						}	
					}
				}
			}
		}, 20, 20);
		
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity damageTaker = e.getEntity();
	
		if (damageTaker instanceof Player) {
		    //DamageTaker is a Player
		    Player taker = (Player) damageTaker;
		    if (damager instanceof Player) {
		        //Damage Causer is also a player
		        Player damagerPlayer = (Player) damager;
		        taker.sendMessage(MSGManager.getMessage(MSGManager.Message.GET_DAMAGE, damagerPlayer.getName(), ChatColor.GOLD, ChatColor.GRAY));
		    }
		}
	}

	@EventHandler
	public void onPlayerWorldChange(PlayerPortalEvent e) {
		Player player = e.getPlayer();
		Location toLocation = e.getTo();
		
		if (toLocation.getWorld().getName().equalsIgnoreCase("world_nether")) {
			Bukkit.broadcastMessage(MSGManager.getMessage(MSGManager.Message.IN_NETHER, player.getName(), ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_RED));
		} else if (toLocation.getWorld().getName().equalsIgnoreCase("world")) {
			Bukkit.broadcastMessage(MSGManager.getMessage(MSGManager.Message.IN_OVERWORLD, player.getName(), ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GREEN));
		}
	}
	
	@EventHandler
	public void onPlayerEnchant(EnchantItemEvent e) {
		Player player = e.getEnchanter();
		Bukkit.broadcastMessage(MSGManager.getMessage(MSGManager.Message.ENCHANTMENT, player.getName(), ChatColor.GOLD, ChatColor.GRAY));
	}
	
	@EventHandler
	public void onPlayerSleep(PlayerBedEnterEvent e) {
		Player player = e.getPlayer();
		PlayerProperties l_CurrentPlayer = vPlayerProperties.get(player);
		if (e.getBedEnterResult() == BedEnterResult.OK) {
			Bukkit.broadcastMessage(MSGManager.getMessage(MSGManager.Message.SLEEP, player.getName(), ChatColor.GOLD, ChatColor.GRAY) + ChatColor.GREEN + " zZz");
			l_CurrentPlayer.setSleeping(true);
		} else {
			if (e.getBedEnterResult() == BedEnterResult.NOT_SAFE) {
				Bukkit.broadcastMessage(MSGManager.getMessage(MSGManager.Message.CANT_SLEEP, player.getName(), ChatColor.GOLD, ChatColor.GRAY));
			}
			l_CurrentPlayer.setSleeping(false);
		}
	}
	
	@EventHandler
	public void onPlayerWakeUp(PlayerBedLeaveEvent e) {
		Player player = e.getPlayer();
		PlayerProperties l_CurrentPlayer = vPlayerProperties.get(player);
		if (ServerManager.IsDay()){
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Giorno " + Main.MyServer.getWorld("world").getFullTime() / 24000));
			player.sendMessage(MSGManager.getMessage(MSGManager.Message.GOOD_MORNING));
		} else {
			ServerManager.SendMessageToAllPlayers(MSGManager.getMessage(MSGManager.Message.WAKE_UP, player.getName(), ChatColor.GOLD, ChatColor.GRAY));
		}
		l_CurrentPlayer.setSleeping(false);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player l_Player = e.getPlayer();
		e.setJoinMessage(MSGManager.getMessage(MSGManager.Message.PLAYER_JOIN, l_Player.getName(), ChatColor.GOLD, ChatColor.GRAY));
		vPlayerProperties.put(l_Player, new PlayerProperties());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player l_Player = e.getPlayer();
		ServerManager.ResetScoreboard(l_Player);
		e.setQuitMessage(MSGManager.getMessage(MSGManager.Message.PLAYER_LEFT, l_Player.getName(), ChatColor.GOLD, ChatColor.GRAY));
		
		// Stop all track running
		vPlayerProperties.get(l_Player).stopTrucking();
		
		vPlayerProperties.remove(l_Player);
		float soundPitch = 1.2f + (float) Math.random() * (2.0f - 1.2f);
		for (Player player : Main.MyServer.getOnlinePlayers()) {
			vPlayerProperties.get(player).getFightingPlayersRequests().remove(l_Player);
			
			if(vPlayerProperties.get(player).getFightingPlayers().contains(l_Player)) {
				player.sendMessage(MSGManager.getMessage(Message.FIGHT_LOOSE, l_Player.getName(), ChatColor.GOLD, ChatColor.GRAY));
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.f, soundPitch);
			}
			vPlayerProperties.get(player).getFightingPlayers().remove(l_Player);
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player l_Player = e.getPlayer();
		Timestamp now = new Timestamp(new Date().getTime());
		
		PlayerProperties l_CurrentProperties = vPlayerProperties.get(l_Player); 
		if(l_CurrentProperties != null) {
			vPlayerProperties.get(l_Player).setAfk(false);
			vPlayerProperties.get(l_Player).setLastMoveTimestamp(now);
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Player deadPlayer = e.getEntity();
		PlayerProperties deadPlayerProp = vPlayerProperties.get(deadPlayer);

		if (deadPlayerProp.isFighting()) {
	        // Keep player inventory and experience level, delete death message
	        e.getDrops().clear();
	        e.setDroppedExp(0);
	        e.setKeepInventory(true);
	        e.setKeepLevel(true);
	        e.setDeathMessage(null);
	        // Send message to all fighters that player died
	        String message = MSGManager.getMessage(Message.FIGHT_LOOSE, deadPlayer.getName(), ChatColor.GOLD, ChatColor.GRAY);
	        deadPlayer.sendMessage(message);
			for(Player p : deadPlayerProp.getFightingPlayers()) {
				p.sendMessage(message);
			}
	        // Respawn player to simulate he is not dead
	        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	                deadPlayer.spigot().respawn();	// Force respawn method
	                deadPlayer.setHealth(deadPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());  // Restore player health to max

	                // Clean variables
	                float soundPitch = 1.2f + (float) Math.random() * (2.0f - 1.2f);
	    			for(Player fightingPlayer : deadPlayerProp.getFightingPlayers()) {
	    				vPlayerProperties.get(fightingPlayer).getFightingPlayers().remove(deadPlayer);
	    				if (vPlayerProperties.get(fightingPlayer).getFightingPlayers().size() == 0) {
	    					fightingPlayer.setHealth(fightingPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()); // Restore player health to max
	    					fightingPlayer.playSound(fightingPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0f, soundPitch);
	    				}
	    			}
	    			deadPlayerProp.getFightingPlayers().clear();
	    	        e.setKeepInventory(false);
	    	        e.setKeepLevel(false);
	            }
	        }, 1L);  // Schedule after 1 tick
		}
	}
	
	// TODO Testare onPlayerDeath e onPlayerRespawnEvent, includere negli eventi già gestiti
	/*
	 * Location prova;
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		Player deathPlayer = e.getEntity().getPlayer();
		prova =  deathPlayer.getLocation();
		plugin.getTrackRunner().unsetTracking(deathPlayer.getUniqueId());
		deathPlayer.sendMessage(deathPlayer.getLocation().toString());
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e){
		Player player = e.getPlayer();
		plugin.getTrackRunner().setTracking(player.getUniqueId(), prova);
		player.sendMessage(ChatColor.GRAY + "Stai seguendo " + ChatColor.GOLD + " punto di morte");
    }
	*/
	
	@EventHandler
    public void onInventoryclick(InventoryClickEvent event){
		if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
			Inventory clickedInventory = event.getClickedInventory();
			InventoryType inventoryType = clickedInventory.getType();
			
			if (inventoryType.equals(InventoryType.CHEST) || inventoryType.equals(InventoryType.BARREL) || inventoryType.equals(InventoryType.ENDER_CHEST) ) {
				Player player = (Player) event.getWhoClicked();
			
				ArrayList<ItemStack> chestInventory = new ArrayList<>();
				ArrayList<ItemStack> chestInventoryCopy = new ArrayList<>();
				
				for (int i = 0; i < clickedInventory.getSize() ; i++) {
					if (clickedInventory.getItem(i) != null) {
						chestInventory.add(clickedInventory.getItem(i));
		        		chestInventoryCopy.add(clickedInventory.getItem(i));							
					}
		    	}
				
				ItemStackComparator l_SortingType = new ItemStackComparator(SortingType.SIMPLE_ASC);
				
				chestInventory.sort(l_SortingType);
		    					
				// Se gi� ordinato, inverti l'ordinamento
				if (chestInventoryCopy.equals(chestInventory)) {
					l_SortingType.setSortingType(SortingType.SIMPLE_DESC);
				} else {
					l_SortingType.setSortingType(SortingType.SIMPLE_ASC);
				}
				
				chestInventory.sort(l_SortingType);
				
		    	// Compact stack
		    	for(int i = 0; i < chestInventory.size() - 1; i++) {
		    		boolean l_CompactAgain = false;
		    		do{
		    			l_CompactAgain = ChestManager.CompactStack(chestInventory.get(i), chestInventory.get(i+1));
		    			chestInventory.sort(l_SortingType);
		    		} while(l_CompactAgain == true);
		    	}
		    	
				chestInventory.sort(l_SortingType);

		    	ItemStack[] sortedInventory = new ItemStack[chestInventory.size()];
				// Prepara e mostra l'inventario aggiornato con l'ordinamento
		    	for(int i = 0; i < sortedInventory.length ; i++) {
		    		sortedInventory[i] = chestInventory.get(i);
		    	}

		    	clickedInventory.setContents(sortedInventory);
			    event.setCancelled(true);
			    player.updateInventory();
			    player.playNote(player.getLocation(), Instrument.CHIME, Note.natural(1, Tone.A));

			}
			
		}
		
	}
	
	@EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
		 Player player = event.getPlayer();
	     String message = event.getMessage();
	     event.setFormat(ChatColor.GOLD + player.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message);
	     
	     for(String messageWord: message.split(" ")) {
	    	 for(String thanksWord: ThanksCommand.THANKS_WORDS){
	    		 if (messageWord.equalsIgnoreCase(thanksWord)) {
	        		 ThanksCommand.makeHeartEffect(player);
	        	 }
	          }
	     }  
	}
	
	/**
	* Returns an HashMap containing all the players that are currently sleeping
	* 
	* @return      HashMap of sleeping players
	*/
	public static HashMap<Player, PlayerProperties> getSleepingPlayers()
	{
		HashMap<Player, PlayerProperties> sleepingPlayers = new HashMap<>();
		for(Map.Entry<Player, PlayerProperties> playerProp : vPlayerProperties.entrySet())
		{
			if(playerProp.getValue().isSleeping())
			{
				sleepingPlayers.put(playerProp.getKey(), playerProp.getValue());
			}
		}
		return sleepingPlayers; 
	}
}
