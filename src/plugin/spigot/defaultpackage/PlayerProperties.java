package plugin.spigot.defaultpackage;

import java.sql.Timestamp;
import java.util.Date;

public class PlayerProperties {
	private Timestamp vLastMoveTimestamp;
	private boolean vAfk;
	private boolean vIsSleeping;

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
	
	public PlayerProperties() {
		setLastMoveTimestamp(new Timestamp(new Date().getTime()));
		setAfk(false);
		setSleeping(false);
	}

}
