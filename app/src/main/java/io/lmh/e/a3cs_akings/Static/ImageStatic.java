package io.lmh.e.a3cs_akings.Static;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by E on 7/29/2018.
 */

public class ImageStatic {
    public static String getFileInString(String coverFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(coverFile);
        byte[] data = getBytesFromBitmap(bitmap);
        Bitmap bitmapOrg = BitmapFactory.decodeByteArray(data, 0, data.length);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String fileString = Base64.encodeToString(ba, Base64.DEFAULT);
        return fileString;
    }

    public static  byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

}
