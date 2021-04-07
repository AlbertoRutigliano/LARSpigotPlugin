package plugin.spigot.defaulpackage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.NullArgumentException;
 
public class PropertiesManager {
	String vPropertiesFilePath;
	Properties vProperties;
 
	public PropertiesManager(String propertiesFilePath)
	{
		if(propertiesFilePath == null)
		{
			throw new NullArgumentException("playerListFilePath");
		}
		
		this.vPropertiesFilePath = propertiesFilePath;
		this.vProperties = new Properties();
		this.ReadProperties();
	}
	
	public void ReadProperties () {
		try {
			InputStream l_InputStream = getClass().getClassLoader().getResourceAsStream(this.vPropertiesFilePath);
 
			if (l_InputStream != null) {
				this.vProperties.load(l_InputStream);
			} else {
				throw new FileNotFoundException("property file '" + this.vPropertiesFilePath + "' not found in the classpath");
			}
			
			l_InputStream.close();

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	
	public String GetValue(String property)
	{
		return this.vProperties.getProperty(property);
	}
}