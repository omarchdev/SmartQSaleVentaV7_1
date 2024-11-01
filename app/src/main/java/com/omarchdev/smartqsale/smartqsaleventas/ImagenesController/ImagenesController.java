package com.omarchdev.smartqsale.smartqsaleventas.ImagenesController;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by OMAR CHH on 22/01/2018.
 */

public class ImagenesController {

    String mCurrentPhotoPath;


    public ImagenesController() {
        mCurrentPhotoPath = "";
    }


    public byte[] PdfToBitmap(File file) {

        String filePath = file.getPath();
        try {
            FileInputStream is = new FileInputStream(file);
            long lent = file.length();
            byte[] bytes = new byte[(int) lent];
            int offset = 0;
            int numRead = 0;


            while (offset < bytes.length && (numRead = is.read(bytes)) >= 0) {
                offset += numRead;
            }
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            String data = Base64.encodeToString(bytes, Base64.DEFAULT);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return convertBitmapToByteArray(bitmap);

    }

    public Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    public Bitmap cambiarResolucion(Bitmap bmp) {
        Bitmap bitmapout = Bitmap.createScaledBitmap(bmp, 250, 250, true);

        bmp = bitmapout;
        return bmp;
    }

    public byte[] convertBitmapToByteArray80(Bitmap bmp) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();

    }

    public byte[] convertBitmapToByteArrayNotCompress(Bitmap bmp) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(bmp.getByteCount());
        bmp.copyPixelsToBuffer(byteBuffer);
        byte[] barray = byteBuffer.array();
        return barray;


    }

    public byte[] convertBitmapToByteArray(Bitmap bmp) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();

    }

    public byte[] convertBitmapToByteArrayPNG(Bitmap bmp) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();

    }

    public Bitmap ConvertByteArrayToBitmap(byte[] bytes) {
        Bitmap bmp;
        if (bytes != null) {
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            bmp = null;
        }
        return bmp;
    }

    public Bitmap ProcesarImagenLogo(Bitmap bmp) {
        Bitmap ouput = bmp;
        ouput = cambiarResolucion(ouput);
        return ouput;
    }

    public Bitmap ProcesarImagen(Bitmap bmp) {
        Bitmap ouput = bmp;
        ouput = cropToSquare(ouput);
        ouput = cambiarResolucion(ouput);
        return ouput;
    }

    public Bitmap textAsBitmap(String texto) {
        if (texto.length() > 1) {
            texto = texto.substring(0, 2);

        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100f);
        paint.setColor(Color.parseColor("#ffffff"));
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(texto) - 1f);
        int height = (int) (baseline + paint.descent() - 1f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(texto, 0, baseline, paint);
        return image;
    }

}
