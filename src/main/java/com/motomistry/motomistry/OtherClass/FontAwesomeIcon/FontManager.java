package com.motomistry.motomistry.OtherClass.FontAwesomeIcon;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Amit on 5/9/2018.
 */
public class FontManager {
    public static final String ROOT = "font/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";
    public static final String RALEWAY_BOLD = ROOT + "Raleway_Bold.ttf",
                                RALEWAY_SEMI_BOLD = ROOT + "Raleway_SemiBold.ttf",
                                MONTSERRAT_BOLD = ROOT + "Montserrat_Bold.ttf";
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}