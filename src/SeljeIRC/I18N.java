package SeljeIRC;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Lars Erik
 */
public class I18N {
    static ResourceBundle lang = ResourceBundle.getBundle("I18N/SeljeIRC", Locale.getDefault());
    
    public static String get(String key)   {
        return lang.getString(key);
    }
}
