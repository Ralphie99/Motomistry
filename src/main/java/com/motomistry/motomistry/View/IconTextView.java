package com.motomistry.motomistry.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;

/**
 * Created by Document on 5/9/2018.
 */

public class IconTextView extends TextView {

    private Context context;

    public IconTextView(Context context) {
        super(context);
        this.context = context;
        createView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView();
    }

    private void createView(){
        setGravity(Gravity.CENTER);
        setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME));
    }
}
