package plugin.spigot.defaulpackage;

public enum CMD {
	
	COORDS("coords"), PLAYERPOS("playerpos"),
	
	ADD("add"), REMOVE("remove"), 
	
	HIDDEN("hidden"), ALL("all");
	
	private String cmd;
	
    private CMD(String cmd) {
        this.cmd = cmd;
    }
   
    @Override
    public String toString(){
        return cmd;
    }
    
    public boolean isEqual(String value) { 
        return this.cmd.equalsIgnoreCase(value);
    }
    
}
