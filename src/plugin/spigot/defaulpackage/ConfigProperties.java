package plugin.spigot.defaulpackage;

public enum ConfigProperties {
	
	SERVER_TEST_PORT("0"), SECONDS_TO_AFK("30");
	
	private String prop;
	
    private ConfigProperties(String prop) {
        this.prop = prop;
    }
   
    @Override
    public String toString(){
        return this.name()+": "+this.prop;
    }
    
    public boolean isEqual(String value) { 
        return this.prop.equalsIgnoreCase(value);
    }
    
}