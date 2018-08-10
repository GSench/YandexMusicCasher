package ru.yandexmusiccasher.presentation.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

import java.io.File;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.model.MusicFile;
import ru.yandexmusiccasher.presentation.utils.FileUtil;
import ru.yandexmusiccasher.presentation.utils.ToastService;

/**
 * Created by grish on 10.08.2018.
 */

public class AMusicFile extends MusicFile {

    private Context context;
    private DocumentFile documentFile;

    public AMusicFile(String id, Context context, DocumentFile documentFile) {
        super(id);
        this.context=context;
        this.documentFile=documentFile;
    }

    @Override
    public void play() {
        String filePath = FileUtil.getFullPathFromDocumentUri(documentFile.getUri(), context);
        if(filePath==null) return;
        File file = new File(filePath);
        System.out.println("Music path: "+filePath);
        Intent player = new Intent(Intent.ACTION_VIEW);
        player.setDataAndType(Uri.fromFile(file), "audio/mp3");
        player.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (player.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(player);
        } else ToastService.show(context.getString(R.string.no_music_app), context);
    }
}
