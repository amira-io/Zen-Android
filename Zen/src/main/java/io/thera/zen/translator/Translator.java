package io.thera.zen.translator;

import io.thera.zen.core.ZenResManager;

/**
 * Created by marcostagni on 02/04/14.
 */
public class Translator {

    public static String T( String id ) {
        return ZenResManager.getString(id);
    }
}
