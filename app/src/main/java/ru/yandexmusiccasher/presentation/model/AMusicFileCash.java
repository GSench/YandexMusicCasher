package ru.yandexmusiccasher.presentation.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.model.MusicFile;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.model.MusicStorage;
import ru.yandexmusiccasher.presentation.utils.ToastService;

/**
 * Created by grish on 10.08.2018.
 */

public class AMusicFileCash extends MusicFileCash {

    private File file;
    private Context context;

    public AMusicFileCash(String id, File file, Context context) {
        super(id);
        this.file=file;
        this.context=context;
    }

    @Override
    public void delete() {
        file.delete();
    }

    @Override
    public MusicFile copyTo(MusicStorage music) throws IOException {
        Uri fileUri = Uri.fromFile(file);
        ContentResolver cR = context.getContentResolver();
        DocumentFile docDir = ((AMusicStorage) music).getDocumentFile();
        DocumentFile newFile = docDir.createFile(cR.getType(fileUri), fileUri.getLastPathSegment());
        InputStream in = null;
        OutputStream out = null;
        try {
            in = cR.openInputStream(fileUri);
            out = cR.openOutputStream(newFile.getUri());
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return new AMusicFile(getId(), context, newFile);
    }

    @Override
    public void play() {
        Intent player = new Intent(Intent.ACTION_VIEW);
        player.setDataAndType(Uri.fromFile(file), "audio/mp3");
        player.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (player.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(player);
        } else ToastService.show(context.getString(R.string.no_music_app), context);
    }
}
