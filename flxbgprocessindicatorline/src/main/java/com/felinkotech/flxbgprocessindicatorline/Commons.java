package com.felinkotech.flxbgprocessindicatorline;

import android.content.Context;

public class Commons {
    public static int dpToPx(Context context, int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
