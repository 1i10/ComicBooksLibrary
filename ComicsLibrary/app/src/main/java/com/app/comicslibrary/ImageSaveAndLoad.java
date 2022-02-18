package com.app.comicslibrary;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageSaveAndLoad {
    private Context context;
    private String directoryName = "imageDir";

    public ImageSaveAndLoad(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String saveToInternalStorage(Bitmap bitmapImage, long index){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/com.app.comicsLibrary/app_data/imageDir
        File directory = cw.getDir(directoryName, Context.MODE_PRIVATE);
        // Create imageDir
        String imageName = "image_" + index + ".jpg";
        File path =new File(directory,imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path, long index)
    {
        Bitmap b = null;
        String imageName = "image_" + index + ".jpg";
        try {
            File f=new File(path, imageName);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    public boolean deleteImageInStorage(long index){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/com.app.comicsLibrary/app_data/imageDir
        File directory = cw.getDir(directoryName, Context.MODE_PRIVATE);
        // Create imageDir
        String imageName = "image_" + index + ".jpg";
        File file =new File(directory,imageName);

        return file.delete();
    }

    public Bitmap resizeImage(Bitmap image){
        int nh = (int) ( image.getHeight() * (512.0 / image.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(image, 512, nh, true);

        return scaled;
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            //Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}
