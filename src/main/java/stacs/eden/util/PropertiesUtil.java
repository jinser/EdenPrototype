package stacs.eden.util;

import hashstacs.sdk.util.StacsUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    //Configuration File Keys
    private static String _configProperties;
    private static FileReader _propertiesFile;
    private static Properties _properties;

    /**
     * returns property files stored in the configuration file
     * @param configProperty
     * @return
     */
    public static String getConfigProperty(String propertiesFileAbsLocation, ConfigEnums configProperty) {
        initializeProperties(propertiesFileAbsLocation);

        //add new properties in the config file here
        switch(configProperty) {
            case MERCHANT_AESKEY:
                return _properties.getProperty(ConfigEnums.MERCHANT_AESKEY.getValue());
            case MERCHANT_ID:
                return _properties.getProperty(ConfigEnums.MERCHANT_ID.getValue());
            case MERCHANT_URL:
                return _properties.getProperty(ConfigEnums.MERCHANT_URL.getValue());
            case BD_CODE:
                    return _properties.getProperty(ConfigEnums.BD_CODE.getValue());
            default:
                break;
        }

        return null;
    }

    /**
     * initializes the configuration properties file
     */
    private static void initializeProperties(String propertiesFileAbsLocation) {
        //if properties file has not been initialized, skip the remainder steps
        if(_configProperties == null) {
            _configProperties = propertiesFileAbsLocation;
        }

        //if properties has already been initialized, skip the remainder steps
        if(_propertiesFile != null | _properties != null) {
            return ;
        }

        //setup the properties needed from the configuration file
        try {
            _propertiesFile = new FileReader(_configProperties);

        } catch (FileNotFoundException e) {

        }

        //update the properties object based on the configuration file
        try {
            _properties = new Properties();
            _properties.load(_propertiesFile);
        } catch (IOException e) {

        }

    }

    public static enum ConfigEnums {

        //add new properties from the config file here, values are as follows: ENUM,key
        MERCHANT_AESKEY("MERCHANT_AESKEY","stacs.merchant.aeskey"),
        MERCHANT_ID("MERCHANT_ID","stacs.merchant.id"),
        MERCHANT_URL("MERCHANT_URL","stacs.merchant.url"),
        BD_CODE("BD_CODE","stacs.bdcode")
        ;

        private final String _property;
        private final String _value;

        ConfigEnums(String key, String value) {
            this._property = key;
            this._value = value;
        }
        public String getProperty() {
            return _property;
        }
        public String getValue() {
            return _value;
        }
    }
}
