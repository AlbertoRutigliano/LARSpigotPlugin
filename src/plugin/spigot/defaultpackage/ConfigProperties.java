package plugin.spigot.defaultpackage;

public enum ConfigProperties {
	
	SERVER_TEST_PORT("0"), SECONDS_TO_AFK("30"), COORDS_FILE("coords.csv"),
	CONFIG_FILE("config.yml"),
	PLUGIN_FOLDER_PATH("./plugins/" + Main.getPlugin(Main.class).getName() + "/");
	
	private String value;
	
    private ConfigProperties(String prop) {
        this.value = prop;
    }
    
    /* 
     * Example using CONFIG_FILE("config.yml"):
     * 	- getPropertyName() return "CONFIG_FILE"
     *  - getValue() return "config.yml"
     */
    public String getPropertyName() {
    	return name();
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