package ru.yandexmusiccasher.presentation.model;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ru.yandexmusiccasher.domain.model.MFile;

/**
 * Created by grish on 29.07.2018.
 */

public class AndroidFile implements MFile {

    private static final int TYPE_URI = 1;
    private static final int TYPE_FILE = 2;
    private int type = 0;
    private boolean isUri(){
        return type==TYPE_URI;
    }
    private boolean isFile(){
        return type==TYPE_FILE;
    }

    private java.io.File file;
    private Uri uri;

    private AndroidFile(){}

    public AndroidFile(Uri uri) {
        this.uri = uri;
        type=TYPE_URI;
    }

    public AndroidFile(File file) {
        this.file = file;
        type=TYPE_FILE;
    }

    public InputStream openInputStream(Context context) throws FileNotFoundException {
        if(isFile()) return new FileInputStream(file);
        if(isUri()) return context.getContentResolver().openInputStream(uri);
        return null;
    }

    public OutputStream openOutputStream(Context context) throws FileNotFoundException {
        if(isFile()) return new FileOutputStream(file);
        if(isUri()) return context.getContentResolver().openOutputStream(uri);
        return null;
    }

    @Override
    public boolean delete(){
        if(isFile()) return file.delete();
        return false;
    }

    @Override
    public String getName(){
        if(isFile()) return file.getName();
        if(isUri()) return new java.io.File(uri.getPath()).getName();
        return null;
    }

    @Override
    public String toString() {
        if(isFile()) return file.getAbsolutePath();
        if(isUri()) return uri.toString();
        return null;
    }

    public String getMimeType(Context context){
        if(isUri()) return context.getContentResolver().getType(uri);
        if(isFile()) {
            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            return type;
        }
        return null;
    }

}
