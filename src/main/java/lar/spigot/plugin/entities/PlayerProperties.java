package lar.spigot.plugin.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerProperties {
	private Timestamp vLastMoveTimestamp;
	private boolean vAfk;
	private boolean vIsSleeping;
	private ArrayList<Player> vFightingPlayers;
	private ArrayList<Player> vFightingPlayersRequests;
	
	public boolean isAfk() {
		return vAfk;
	}

	public void setAfk(boolean vAfk) {
		this.vAfk = vAfk;
	}

	public boolean isSleeping() {
		return vIsSleeping;
	}

	public void setSleeping(boolean vIsSleeping) {
		this.vIsSleeping = vIsSleeping;
	}
	
	public Timestamp getLastMoveTimestamp() {
		return vLastMoveTimestamp;
	}

	public void setLastMoveTimestamp(Timestamp vLastMoveTimestamp) {
		this.vLastMoveTimestamp = vLastMoveTimestamp;
	}
	
	public ArrayList<Player> getFightingPlayers() {
		return vFightingPlayers;
	}

	public void setFightingPlayers(ArrayList<Player> vFightingPlayers) {
		this.vFightingPlayers = vFightingPlayers;
	}

	public boolean isFighting() {
		if (getFightingPlayers() == null || getFightingPlayers().size() == 0) {
			return false;
		}
		return true;
	}

	public ArrayList<Player> getFightingPlayersRequests() {
		return vFightingPlayersRequests;
	}

	public void setFightingPlayersRequests(ArrayList<Player> FightingPlayersRequests) {
		this.vFightingPlayersRequests = FightingPlayersRequests;
	}

	public PlayerProperties() {
		setLastMoveTimestamp(new Timestamp(new Date().getTime()));
		setAfk(false);
		setSleeping(false);
		setFightingPlayers(new ArrayList<Player>());
		setFightingPlayersRequests(new ArrayList<Player>());
	}

}
