package ru.yandexmusiccasher.presentation.model;

import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

import ru.yandexmusiccasher.domain.model.MusicFile;
import ru.yandexmusiccasher.domain.model.MusicStorage;

/**
 * Created by grish on 10.08.2018.
 */

public class AMusicStorage extends MusicStorage {

    public AMusicStorage(Uri documentFileUri, Context context) {
        documentFile = DocumentFile.fromTreeUri(context, documentFileUri);
        this.context = context;
    }

    private DocumentFile documentFile;
    private Context context;

    public DocumentFile getDocumentFile(){
        return documentFile;
    }

    @Override
    public MusicFile findById(String id) {
        for (DocumentFile file: documentFile.listFiles())
            if(file.getName().contains(id)) return new AMusicFile(id, context, file);
        return null;
    }

}
