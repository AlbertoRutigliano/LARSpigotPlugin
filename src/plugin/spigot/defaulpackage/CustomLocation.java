package plugin.spigot.defaulpackage;

import java.io.Serializable;

//TODO Alberto, analizzare, decidere, sistemare

public class CustomLocation implements Serializable {

	private static final long serialVersionUID = 1L;
	private double x, y, z;
	private String name;
	private boolean hidden = false;
	
	public CustomLocation(String name, double x, double y, double z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hidden = false;
	}
	
	public CustomLocation(String name, double x, double y, double z, boolean hidden) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hidden = hidden;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
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

	public double calculateDistance(CustomLocation xz2) {
        
	    double ac = Math.abs(xz2.getZ() - this.getZ());
	    double cb = Math.abs(xz2.getX() - this.getX());
	        
	    return Math.hypot(ac, cb);
	}
	
}
