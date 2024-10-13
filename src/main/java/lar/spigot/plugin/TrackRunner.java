package lar.spigot.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import lar.spigot.plugin.managers.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * The runner for the compass. Thanks to LADBukkit (Robin Eschbach)
 */
public class TrackRunner extends BukkitRunnable {

	// Hash map containing the tracking information.
	//private final HashMap<UUID, Location> tracking = new HashMap<>();

	private UUID uuid;
	private Location targetLocation = null;
	private UUID targetPlayerUUID = null;
	private boolean isTracking = true;
	private String trackFormat = ChatColor.WHITE + "%left%" + ChatColor.GOLD + " %distance% blocchi " + ChatColor.WHITE
			+ "%right%";
	
	public TrackRunner(UUID uuid, Location location) {
		this.uuid = uuid;
		this.targetLocation = location.clone();
		this.targetPlayerUUID = null; 
		this.isTracking = true;
	}
	
	public TrackRunner(UUID uuid, UUID targetPlayerUUID) {
		this.uuid = uuid;
		this.targetLocation = null; 
		this.targetPlayerUUID = targetPlayerUUID; 
		this.isTracking = true;
	}

	@Override
	public void run() {
		Player p = Bukkit.getPlayer(uuid);

		if (!isTracking) {
			PlayerManager.vPlayerProperties.get(p).stopTrucking();
			this.cancel();
			return;
		}
		
		if (p != null && p.isOnline()) {
			Location destinationLocation = followingLocation(); 
			if (destinationLocation != null) {
				destinationLocation.setY(0);
				if (isSameWorld(p, destinationLocation)) {
					Location playerLoc = p.getLocation().clone();
					playerLoc.setY(0);

					double xDiff = destinationLocation.getX() - playerLoc.getX();
					double zDiff = destinationLocation.getZ() - playerLoc.getZ();
					double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

					if (distance < 10) {
						p.sendMessage(ChatColor.GRAY + "Sei arrivato a destinazione");
						isTracking = false;
						PlayerManager.vPlayerProperties.get(p).stopTrucking();
						this.cancel();
						return;
					}
					
					StringBuilder right = new StringBuilder();
					StringBuilder left = new StringBuilder();

					// calculate angle difference
					Vector diffVec = destinationLocation.toVector().subtract(playerLoc.toVector());
					diffVec.normalize();
					double angle = Math.toDegrees(Math.atan2(diffVec.getX(), diffVec.getZ()));
					double diff = angleDifference(convertToAngle(angle), 360 - convertToAngle(playerLoc.getYaw()));

					int point = (int) Math.min(Math.abs(diff / 9), 9);

					// create the compass
					for (int i = 1; i <= 9; i++) {
						if (i == point) {
							if (diff < 0) {
								right.append(">");
								left.append(" ");
							} else {
								right.append(" ");
								left.append("<");
							}
						} else {
							right.append(" ");
							left.append(" ");
						}
					}

					String action = trackFormat.replace("%distance%", Integer.toString((int) distance))
							.replace("%right%", right).replace("%left%", left.reverse());
					sendActionBar(p, action);
				} else {
					sendActionBar(p, "Mondo sbagliato");
				}
			} else {
				PlayerManager.vPlayerProperties.get(p).stopTrucking();
				this.cancel();
				return;
			}
			
		}
	}

	/**
	 * Sends the action bar to a player.
	 * 
	 * @param p    The player.
	 * @param text The text of the action bar.
	 */
	private void sendActionBar(Player p, String text) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
	}

	/**
	 * Wraps an angle to -180 => 180 of a player to an angle.
	 * 
	 * @param yaw The yaw of the player.
	 * @return The angle.
	 */
	private double convertToAngle(double yaw) {
		// wrap yaw
		double x = (((yaw + 180) % 360) + 360) % 360 - 180;
		return x + 180;
	}

	/**
	 * Calculates the shortest difference of two angles
	 * 
	 * @param a The first angle.
	 * @param b The second angle.
	 * @return The difference
	 */
	private double angleDifference(double a, double b) {
		double d = Math.abs(a - b) % 360;
		double r = d > 180 ? 360 - d : d;
		int sign = (a - b >= 0 && a - b <= 180) || (a - b <= -180 && a - b >= -360) ? 1 : -1;
		return r * sign;
	}
	
	private boolean isSameWorld(Player player, Location location) {
		return location.getWorld().getName().equals(player.getWorld().getName());
	}
	
	private Location followingLocation() {
		if (this.targetPlayerUUID != null) {
			Player targetPlayer = Bukkit.getPlayer(targetPlayerUUID);
			if (targetPlayer != null && targetPlayer.isOnline()) {
				return targetPlayer.getLocation();
			}
		} else if (this.targetLocation != null) {
			return targetLocation;
		}
		return null;
	}
	
	public void stopTracking() {
		this.isTracking = false;
		this.targetLocation = null;
		this.targetPlayerUUID = null;
	}

}
