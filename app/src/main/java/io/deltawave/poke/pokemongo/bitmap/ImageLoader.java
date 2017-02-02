package io.deltawave.poke.pokemongo.bitmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

import io.deltawave.poke.pokemongo.bitmap.rawscale.RawScaleConverter;

/**
 * Created by will on 8/7/16.
 */
public class ImageLoader {

    private static String POKE_FOLDER = "poke_x_y/";

    public static String ORIENTATION_FRONT = "front/";
    public static String ORIENTATION_BACK = "back/";
    public static String TYPE_NORMAL = "normal/";
    public static String TYPE_SHINY = "shiny/";

    private static String FILE_EXTENSION = ".png";

    private static String TRAINER_FOLDER = "trainer/";

    public static int DOWN_0 = 0;
    public static int DOWN_1 = 1;
    public static int DOWN_2 = 2;
    public static int DOWN_3 = 3;
    public static int LEFT_0 = 4;
    public static int LEFT_1 = 5;
    public static int LEFT_2 = 6;
    public static int LEFT_3 = 7;
    public static int UP_0 = 8;
    public static int UP_1 = 9;
    public static int UP_2 = 10;
    public static int UP_3 = 11;
    public static int RIGHT_0 = 12;
    public static int RIGHT_1 = 13;
    public static int RIGHT_2 = 14;
    public static int RIGHT_3 = 15;

    public static Bitmap getBitmapAsset(Context context, String path) {
        AssetManager assetManager = context.getAssets();

        InputStream inputStream;
        Bitmap bitmap = null;
        try {
            inputStream = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException ex) {
            System.out.println("Error while opening asset image");
            ex.printStackTrace();
            System.exit(1);
        }

        return bitmap;
    }

    public static Bitmap getPokeBitmap(Context context, int pokeNum, String orientation, String type) {
        Bitmap bitmap = getBitmapAsset(context, POKE_FOLDER + orientation + type + pokeNum + FILE_EXTENSION);

        return bitmap;
    }

    public static Bitmap getPokeBitmap4x(Context context, int pokeNum, String orientation, String type) {
        return RawScaleConverter.scaleResource(
                RawScaleConverter.scaleResource(
                        getPokeBitmap(context, pokeNum, orientation, type)
                )
        );
    }

    public static Bitmap get4x(Context context, Bitmap bitmap) {
        return RawScaleConverter.scaleResource(
                RawScaleConverter.scaleResource(
                        bitmap
                )
        );
    }

    public static Bitmap[] getCharacterImages(Context ctx, String trainerName) {
        Bitmap bitmap = getBitmapAsset(ctx, TRAINER_FOLDER + trainerName + FILE_EXTENSION);

        Bitmap[] bitmaps = new Bitmap[16];

        bitmaps[DOWN_0] = get4x(ctx, Bitmap.createBitmap(bitmap, 0, 0, 64, 64));
        bitmaps[DOWN_1] = get4x(ctx, Bitmap.createBitmap(bitmap, 64, 0, 64, 64));
        bitmaps[DOWN_2] = get4x(ctx, Bitmap.createBitmap(bitmap, 128, 0, 64, 64));
        bitmaps[DOWN_3] = get4x(ctx, Bitmap.createBitmap(bitmap, 192, 0, 64, 64));

        bitmaps[LEFT_0] = get4x(ctx, Bitmap.createBitmap(bitmap, 0, 64, 64, 64));
        bitmaps[LEFT_1] = get4x(ctx, Bitmap.createBitmap(bitmap, 64, 64, 64, 64));
        bitmaps[LEFT_2] = get4x(ctx, Bitmap.createBitmap(bitmap, 128, 64, 64, 64));
        bitmaps[LEFT_3] = get4x(ctx, Bitmap.createBitmap(bitmap, 192, 64, 64, 64));

        bitmaps[UP_0] = get4x(ctx, Bitmap.createBitmap(bitmap, 0, 128, 64, 64));
        bitmaps[UP_1] = get4x(ctx, Bitmap.createBitmap(bitmap, 64, 128, 64, 64));
        bitmaps[UP_2] = get4x(ctx, Bitmap.createBitmap(bitmap, 128, 128, 64, 64));
        bitmaps[UP_3] = get4x(ctx, Bitmap.createBitmap(bitmap, 192, 128, 64, 64));

        bitmaps[RIGHT_0] = get4x(ctx, Bitmap.createBitmap(bitmap, 0, 192, 64, 64));
        bitmaps[RIGHT_1] = get4x(ctx, Bitmap.createBitmap(bitmap, 64, 192, 64, 64));
        bitmaps[RIGHT_2] = get4x(ctx, Bitmap.createBitmap(bitmap, 128, 192, 64, 64));
        bitmaps[RIGHT_3] = get4x(ctx, Bitmap.createBitmap(bitmap, 192, 192, 64, 64));

        return bitmaps;
    }


}
