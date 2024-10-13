package lar.spigot.plugin.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.entity.Player;

import lar.spigot.plugin.TrackRunner;

public class PlayerProperties {
	private Timestamp vLastMoveTimestamp;
	private boolean vAfk;
	private boolean vIsSleeping;
	private ArrayList<Player> vFightingPlayers;
	private ArrayList<Player> vFightingPlayersRequests;
	private TrackRunner trackRunner = null;
	
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
	
	public TrackRunner getTrackRunner() {
		return trackRunner;
	}

	public void setTrackRunner(TrackRunner trackRunner) {
		this.trackRunner = trackRunner;
	}
	
	public boolean isTracking() {
		return this.trackRunner != null;
	}
	
	public void stopTrucking() {
		this.trackRunner = null;
	}

	public PlayerProperties() {
		setLastMoveTimestamp(new Timestamp(new Date().getTime()));
		setAfk(false);
		setSleeping(false);
		setFightingPlayers(new ArrayList<Player>());
		setFightingPlayersRequests(new ArrayList<Player>());
	}

}
