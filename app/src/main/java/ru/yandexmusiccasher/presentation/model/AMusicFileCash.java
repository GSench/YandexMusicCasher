package ru.yandexmusiccasher.presentation.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.model.MusicFile;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.model.MusicInfo;
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
    public void setMusicInfo(MusicInfo musicInfo) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        Mp3File mp3file = new Mp3File(file.getAbsolutePath());

        //clearing all existing tags
        if (mp3file.hasId3v1Tag()) {
            mp3file.removeId3v1Tag();
        }
        if (mp3file.hasId3v2Tag()) {
            mp3file.removeId3v2Tag();
        }
        if (mp3file.hasCustomTag()) {
            mp3file.removeCustomTag();
        }

        //creating new id3v2 tags
        ID3v2 id3v2Tag = new ID3v24Tag();
        mp3file.setId3v2Tag(id3v2Tag);

        //adding id3v2 tags
        if(musicInfo.track!=null) id3v2Tag.setTrack(musicInfo.track);
        if(musicInfo.artist!=null) id3v2Tag.setArtist(musicInfo.artist);
        if(musicInfo.title!=null) id3v2Tag.setTitle(musicInfo.title);
        if(musicInfo.album!=null) id3v2Tag.setAlbum(musicInfo.album);
        if(musicInfo.year!=null) id3v2Tag.setYear(musicInfo.year);
        if(musicInfo.genre!=null) id3v2Tag.setGenreDescription(musicInfo.genre);
        if(musicInfo.comment!=null) id3v2Tag.setComment(musicInfo.comment);
        //if(musicInfo.lyrics!=null) id3v2Tag.setLyrics(musicInfo.lyrics);
        if(musicInfo.composer!=null) id3v2Tag.setComposer(musicInfo.composer);
        if(musicInfo.publisher!=null) id3v2Tag.setPublisher(musicInfo.publisher);
        if(musicInfo.albumArtist!=null) id3v2Tag.setOriginalArtist(musicInfo.originalArtist);
        if(musicInfo.artist!=null) id3v2Tag.setAlbumArtist(musicInfo.artist);
        if(musicInfo.copyright!=null) id3v2Tag.setCopyright(musicInfo.copyright);
        if(musicInfo.url!=null) id3v2Tag.setUrl(musicInfo.url);
        if(musicInfo.encoder!=null) id3v2Tag.setEncoder(musicInfo.encoder);
        if(musicInfo.albumArtwork!=null) id3v2Tag.setAlbumImage(musicInfo.albumArtwork, musicInfo.albumArtworkMime);

        String newName = file.getName();
        newName = newName.substring(0, newName.lastIndexOf("album"))+"-tagged-"+newName.substring(newName.lastIndexOf("album"));
        File newFile = new File(file.getParentFile(), newName);
        mp3file.save(newFile.getAbsolutePath());
        file.delete();
        file=newFile;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public void renameTo(String newName) {
        file.renameTo(new File(file.getParentFile(), newName));
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

    public String getFullPath() {
        return file.getAbsolutePath();
    }
}
