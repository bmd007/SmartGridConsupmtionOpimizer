package ir.tiroon.schedulingApp.CustomLayout;

import android.content.Context;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Created by Lenovo on 17/12/2016.
 */
public class CustomTabLayout extends TabLayout {

    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                setTabMode(MODE_SCROLLABLE);
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                setTabMode(MODE_SCROLLABLE);
                break;
            default: {

                if (getTabCount()<6)
                    setTabMode(MODE_FIXED);
                else
                    setTabMode(MODE_SCROLLABLE);

                break;
            }

        }

    }

}
