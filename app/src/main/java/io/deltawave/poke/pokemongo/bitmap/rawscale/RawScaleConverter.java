package io.deltawave.poke.pokemongo.bitmap.rawscale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * Created by will on 8/7/16.
 */
public class RawScaleConverter {

    public static Bitmap scaleResource(Bitmap bitmap) {
        int[] pixels = new int[bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        RawScale3x rs3 = new RawScale3x(pixels, bitmap.getWidth(), bitmap.getHeight());

        Bitmap newBitmap = Bitmap.createBitmap(rs3.getScaledData(), bitmap.getWidth()*3, bitmap.getHeight()*3, Bitmap.Config.ARGB_8888);
        return newBitmap;
    }

    public static Bitmap scaleResource(Context context, int resId) {
        Resources r = context.getResources();

        Options options = new Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(r, resId, options);
        return scaleResource(bitmap);
    }

}
