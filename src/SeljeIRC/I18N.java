package SeljeIRC;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Framework for getting strings from the .properties-files. Tidier than including ResourceBundle in EVERY file that needs it...
 * @author Lars Erik Pedersen
 * @version 0.1
 * @since 0.1
 */
public class I18N {
    static ResourceBundle lang = ResourceBundle.getBundle("I18N/SeljeIRC", Locale.getDefault());    // Get the langange pack, make it staic so everyone can get it
    
    /**
     * Fetch a string from the language pack
     * @param key Keyword of the string you want to fetch
     * @return The string
     */
    public static String get(String key)   {
        return lang.getString(key);
    }
}
