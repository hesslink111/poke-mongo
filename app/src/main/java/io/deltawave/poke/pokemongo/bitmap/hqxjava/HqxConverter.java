package io.deltawave.poke.pokemongo.bitmap.hqxjava;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by will on 8/7/16.
 */
public class HqxConverter {
    public static Bitmap scaleResource(Context context, int resId) {
        Resources r = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(r, resId);

        Bitmap alpha = bitmap.extractAlpha();

        int[] pixels = new int[bitmap.getHeight()*bitmap.getWidth()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        // Initialize lookup tables
        RgbYuv.hqxInit();

        // Obtain pixel data for destination image
        //int[] dataDest = new int[bitmap.getWidth()*2*bitmap.getHeight()*2];
        int[] dataDest = new int[bitmap.getWidth()*4*bitmap.getHeight()*4];
        // Resize it
        //Hqx_2x.hq2x_32_rb(pixels, dataDest, bitmap.getWidth(), bitmap.getHeight());
        Hqx_4x.hq4x_32_rb(pixels, dataDest, bitmap.getWidth(), bitmap.getHeight());
        // Save our result
        Bitmap newBitmap = Bitmap.createBitmap(dataDest, bitmap.getWidth()*4, bitmap.getHeight()*4, Bitmap.Config.ARGB_8888);

        // More calls to hq[234]x_32_rb() methods
        // ....

        // Release the lookup table
        RgbYuv.hqxDeinit();

        return newBitmap;
    }
}
