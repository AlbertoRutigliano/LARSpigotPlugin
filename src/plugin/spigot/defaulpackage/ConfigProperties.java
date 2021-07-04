package plugin.spigot.defaulpackage;

public enum ConfigProperties {
	
	SERVER_TEST_PORT("0"), SECONDS_TO_AFK("30"), COORDS_FILE("./coords.csv");
	
	private String value;
	
    private ConfigProperties(String prop) {
        this.value = prop;
    }
   
    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEqual(String value) { 
        return this.value.equalsIgnoreCase(value);
    }
	
	@Override
    public String toString(){
        return this.name() + ": " + this.value;
    }
    
}