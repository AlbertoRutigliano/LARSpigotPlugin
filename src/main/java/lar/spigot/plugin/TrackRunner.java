package lar.spigot.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * The runner for the compass.
 * Thanks to LADBukkit (Robin Eschbach)
 */
public class TrackRunner extends BukkitRunnable {

    // Hash map containing the tracking information.
    private final HashMap<UUID, Location> tracking = new HashMap<>();

    @Override
    public void run() {
        // A set with all done players to dodge a concurrent modification when a player is at the destination
        Set<UUID> done = new HashSet<>();

        String trackFormat = ChatColor.WHITE + "%left%" + ChatColor.GOLD + " %distance% blocchi " + ChatColor.WHITE + "%right%";
        tracking.forEach((uuid, location) -> {
            Player p = Bukkit.getPlayer(uuid);
            if(p != null && p.isOnline()) {
                if(location.getWorld().getName().equals(p.getWorld().getName())) {
                	Location destinationLoc = location.clone();
                    destinationLoc.setY(0);

                    Location playerLoc = p.getLocation().clone();
                    playerLoc.setY(0);
                    
                    double xDiff = destinationLoc.getX() - playerLoc.getX();
                    double zDiff = destinationLoc.getZ() - playerLoc.getZ();
                    double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

                    StringBuilder right = new StringBuilder();
                    StringBuilder left = new StringBuilder();

                    // calculate angle difference
                    Vector diffVec = destinationLoc.toVector().subtract(playerLoc.toVector());
                    diffVec.normalize();
                    double angle = Math.toDegrees(Math.atan2(diffVec.getX(), diffVec.getZ()));
                    double diff = angleDifference(convertToAngle(angle), 360 - convertToAngle(playerLoc.getYaw()));

                    int point = (int) Math.min(Math.abs(diff / 9), 9);

                    // create the compass
                    for(int i = 1; i <= 9; i++) {
                        if(i == point) {
                            if(diff < 0) {
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

                    if (distance < 10) {
                        p.sendMessage(ChatColor.GRAY + "Sei arrivato a destinazione");
                        done.add(uuid);
                        return;
                    }
                    
                   
                    
                    String action = trackFormat.replace("%distance%", Integer.toString((int) distance)).replace("%right%", right).replace("%left%", left.reverse());
                    sendActionBar(p,  action);
                } else {
                    sendActionBar(p, "Mondo sbagliato");
                }
            }
        });

        // Remove done players from tracking
        done.forEach(this::unsetTracking);
    }

    /**
     * Sends the action bar to a player.
     * @param p The player.
     * @param text The text of the action bar.
     */
    private void sendActionBar(Player p, String text) {	
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
    }

    /**
     * Wraps an angle to -180 => 180 of a player to an angle.
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
     * @param a The first angle.
     * @param b The second angle.
     * @return The difference
     */
    private double angleDifference(double a, double b) {
        double d = Math.abs(a - b) % 360;
        double r = d > 180 ? 360 - d : d;
        int sign = (a - b >= 0 && a - b <= 180) || (a - b <=-180 && a- b>= -360) ? 1 : -1;
        return r * sign;
    }

    /**
     * Sets a player to track a location.
     * @param uuid The uuid of the player.
     * @param location The location.
     */
    public void setTracking(UUID uuid, Location location) {
        Location loc = location.clone();
        loc.setY(0);
        tracking.put(uuid, location);
    }

    /**
     * Removes a player from the tracking map.
     * @param uuid The uuid of the player.
     */
    public void unsetTracking(UUID uuid) {
        tracking.remove(uuid);
        Player p = Bukkit.getPlayer(uuid);
        if(p != null && p.isOnline()) {
            sendActionBar(p, "");
        }
    }

    /**
     * Checks whether a player is currently tracking.
     * @param uuid The uuid of the player.
     * @return Whether the player is tracking.
     */
    public boolean isTracking(UUID uuid) {
        return tracking.containsKey(uuid);
    }
}
