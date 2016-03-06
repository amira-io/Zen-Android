/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.translator;

import io.amira.zen.core.ZenResManager;

public class ZenTranslator {

    public static String T( String id ) {
        return ZenResManager.getString(id);
    }
}
