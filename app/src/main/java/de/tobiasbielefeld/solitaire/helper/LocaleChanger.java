/*
 * Copyright (C) 2016  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */

package de.tobiasbielefeld.solitaire.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import static de.tobiasbielefeld.solitaire.SharedData.*;

/**
 * Update locale in the activities,
 * created with this guide: http://gunhansancar.com/change-language-programmatically-in-android/
 * <p>
 * Created by gunhansancar on 07/10/15.
 * And updated to fit my needs.
 */

public class LocaleChanger {

    private static Locale defaultLocale;

    public static Context onAttach(Context context) {
        return setLocale(context);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static Context setLocale(Context context) {
        String language = getLanguage(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }


    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_KEY_LANGUAGE, defaultLanguage);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale;

        if (language.equals(DEFAULT_LANGUAGE)) {
            locale = defaultLocale;
        } else {
            locale = new Locale(language);
            Locale.setDefault(locale);
        }

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale;

        if (language.equals(DEFAULT_LANGUAGE)) {
            locale = defaultLocale;
        } else {
            locale = new Locale(language);
            Locale.setDefault(locale);
        }

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static void setDefaultLocale(Locale locale) {
        defaultLocale = locale;
    }
}