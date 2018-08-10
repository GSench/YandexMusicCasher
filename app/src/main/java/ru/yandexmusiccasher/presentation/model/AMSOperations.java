package ru.yandexmusiccasher.presentation.model;

import android.content.Context;
import android.net.Uri;

import java.io.FileNotFoundException;

import ru.yandexmusiccasher.domain.model.MusicCash;
import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.domain.model.MusicStorageOperations;
import ru.yandexmusiccasher.presentation.AndroidInterface;

/**
 * Created by grish on 10.08.2018.
 */

public class AMSOperations implements MusicStorageOperations {

    public static final String STORAGE_URI = "storage_uri";

    private Context context;

    public AMSOperations(Context context){
        this.context=context;
    }

    @Override
    public MusicCash getMusicCash() {
        return new AMusicCash(context);
    }

    @Override
    public MusicStorage getMusicStorage() throws FileNotFoundException {
        String uri = context.getSharedPreferences(AndroidInterface.SPREF, Context.MODE_PRIVATE).getString(STORAGE_URI, null);
        if(uri==null) throw new FileNotFoundException();
        return new AMusicStorage(Uri.parse(uri), context);
    }

    @Override
    public void saveMusicStorage(MusicStorage storage) {
        String savingPath = ((AMusicStorage)storage).getDocumentFile().getUri().toString();
        context.getSharedPreferences(AndroidInterface.SPREF, Context.MODE_PRIVATE)
                .edit()
                .putString(STORAGE_URI, savingPath)
                .commit();
    }
}
