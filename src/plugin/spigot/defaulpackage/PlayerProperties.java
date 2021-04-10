package plugin.spigot.defaulpackage;

import java.sql.Timestamp;
import java.util.Date;

public class PlayerProperties {
	private Timestamp vLastMoveTimestamp;
	private boolean vAfk;

	public boolean isAfk() {
		return vAfk;
	}

	public void setAfk(boolean vAfk) {
		this.vAfk = vAfk;
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
	}
}
