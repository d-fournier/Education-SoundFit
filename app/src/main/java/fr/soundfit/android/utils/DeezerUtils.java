package fr.soundfit.android.utils;

import com.deezer.sdk.model.Playlist;

/**
 * Created by Donovan on 13/02/2015.
 */
public class DeezerUtils {
    public static boolean isPlaylistAvailable(Playlist p){
        return !p.getTitle().startsWith("_");
    }
}
