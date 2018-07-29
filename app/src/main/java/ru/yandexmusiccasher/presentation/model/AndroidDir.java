package ru.yandexmusiccasher.presentation.model;

import android.content.Context;
import android.content.UriPermission;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ru.yandexmusiccasher.domain.model.Directory;

/**
 * Created by grish on 29.07.2018.
 */

public class AndroidDir implements Directory {

    private static final int TYPE_URI = 1;
    private static final int TYPE_FILE = 2;
    private int type = 0;
    private boolean isUri(){
        return type==TYPE_URI;
    }
    private boolean isFile(){
        return type==TYPE_FILE;
    }
    private Uri uri;
    private Context context;
    private File file;

    private AndroidDir(){}

    public AndroidDir(Uri uri, Context context) {
        this.uri = uri;
        this.context=context;
        type=TYPE_URI;
    }

    public AndroidDir(File file) {
        this.file = file;
        type=TYPE_FILE;
    }

    @Override
    public boolean available(){
        if(isFile()) return file.exists()&&file.isDirectory();
        if(isUri()) {
            if(!uri.toString().startsWith("content://com.android.externalstorage.documents/tree/")) return false;
            List<UriPermission> uriPermissions = context.getContentResolver().getPersistedUriPermissions();
            for(UriPermission permission: uriPermissions){
                if(permission.getUri().toString().equals(uri.toString())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public AndroidFile createFile(String filename) throws IOException {
        if(isFile()){
            File newFile = new File(file, filename);
            if(file.createNewFile()) return new AndroidFile(newFile);
            else return null;
        }
        if(isUri()){
            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(filename);
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            int dot = filename.lastIndexOf(".");
            if(dot<0) dot=filename.length();
            String name = filename.substring(0, dot);

            DocumentFile docDir = DocumentFile.fromTreeUri(context, uri);
            DocumentFile newFile = docDir.createFile(type, name);
            if(newFile.canRead()) return new AndroidFile(newFile.getUri());
        }
        return null;
    }

    public AndroidFile[] getListFiles(){
        if(isFile()){
            File[] listFiles = file.listFiles();
            AndroidFile[] list = new AndroidFile[listFiles.length];
            for(int  i=0; i<list.length; i++) list[i]=new AndroidFile(listFiles[i]);
            return list;
        }
        if(isUri()){
            DocumentFile[] listFiles = DocumentFile.fromTreeUri(context, uri).listFiles();
            AndroidFile[] list = new AndroidFile[listFiles.length];
            for(int  i=0; i<list.length; i++) list[i]=new AndroidFile(listFiles[i].getUri());
            return list;
        }
         return null;
    }

    @Override
    public String toString() {
        if(isFile()) return file.getAbsolutePath();
        if(isUri()) return uri.toString();
        return null;
    }

}
