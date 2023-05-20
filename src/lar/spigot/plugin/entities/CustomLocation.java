package lar.spigot.plugin.entities;

import java.io.Serializable;

public class CustomLocation implements Serializable {
	
	public enum WorldName { world, world_nether, world_the_end }
	private static final long serialVersionUID = 1L;
	private int x, y, z;
	private String name;
	private boolean hidden = false;
	private WorldName worldName;
	
	public CustomLocation(String name, int x, int y, int z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hidden = false;
	}
	
	public CustomLocation(String name, int x, int y, int z, boolean hidden) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hidden = hidden;
	}
	
	public CustomLocation(String name, int x, int y, int z, boolean hidden, WorldName worldName) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hidden = hidden;
		this.worldName = worldName;
	}
	
	public CustomLocation(String name, int x, int y, int z, boolean hidden, String worldName) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hidden = hidden;
		this.worldName = WorldName.valueOf(worldName);
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name + " " + x + " " + y + " " + z;
	} 
	
	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public WorldName getWorldName() {
		return worldName;
	}
	
	public String getWorldNameString() {
		switch (worldName) {
			case world:
				return "WORLD";
			case world_nether:
				return "NETHER";
			case world_the_end:
				return "END";
			default:
				return "WORLD";
		}
	}

	public void setWorldName(WorldName worldName) {
		this.worldName = worldName;
	}

	public double calculateDistance(CustomLocation xz2) {
        
	    int ac = Math.abs(xz2.getZ() - this.getZ());
	    int cb = Math.abs(xz2.getX() - this.getX());
	        
	    return Math.hypot(ac, cb);
	}
	
}
