import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Property manager use to handle about password to login to manage page.
 * @author Thanakrit Daorueang,Piyaphol Wiengperm
 * */
public class PropertyManager {
    public static final String DATA_FILE = "data/password.properties";

    /**
     * A Logger object is used to log messages for a specific system or application component.
     */
    private static Logger log = null;

    /**
     * The Properties class represents a persistent set of properties.
     */
    private static Properties properties = null;

    /**
     * no-arg constructor
     */
    public PropertyManager() {

    }

    /**
     * A factory method for create a new Logger or return a suitable existing Logger.
     * @return new Logger or return a suitable existing Logger.
     */
    private static Logger getLogger() {
        if ( log == null ) log = Logger.getLogger(PropertyManager.class.getName());
        return log;
    }

    /**
     * Get the property with the specified key in this property list.
     * @return property list.
     */
    public static Properties getProperties(){
        if(properties != null) return properties;
        String filename = System.getProperty( DATA_FILE, DATA_FILE );
        properties = new Properties();
        getLogger().info("Loading properties from " + filename);

        ClassLoader loader = PropertyManager.class.getClassLoader();
        try (InputStream instream = loader.getResourceAsStream(filename);) {

            properties.load(instream);
            getLogger().info("Number of properties read: " + properties.size());

            return properties;
        }catch (FileNotFoundException fe){
            getLogger().warning( "Couldn't find properties file "+filename);

        } catch (IOException ioe){
            getLogger().log(Level.SEVERE, "I/O exception reading properties file "+filename, ioe);
        }

        try (InputStream instream = new FileInputStream(filename)){

            properties.load(instream);

        }catch (FileNotFoundException fe){
            getLogger().warning( "Couldn't find properties file "+filename);

        } catch (IOException ioe){
            getLogger().log(Level.SEVERE, "I/O exception reading properties file "+filename, ioe);
        }

        getLogger().info("Number of properties read: "+properties.size() );

        return properties;
    }

    /**
     * Get String from a factory property method.
     * @param name is name in property file.
     * @param defaultValue is default name.
     * @return value in property file, as a String.
     */
    public static String getProperty(String name, String defaultValue){
        if(properties == null) getProperties();
        return properties.getProperty(name, defaultValue);
    }

    /**
     * Get String from a factory property method.
     * @param name is name in property file.
     * @return value in property file, as a String.
     */
    public static String getProperty(String name){
        if(properties == null) getProperties();
        return properties.getProperty(name);
    }

    /**
     * Set property from key in property file.
     * @param key is key in property file.
     * @param value is value for setting.
     */
    public static void setProperty(String key, String value){
        if(properties == null) getProperties();
        properties.setProperty(key, value);
    }

    /**
     * Test Properties class
     */
    public static void main(String[] args) {
        Properties property = PropertyManager.getProperties();
        if(property == null) {
        }
        else {
            property.list(System.out);
        }

    }

}
