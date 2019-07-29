package kr.entree.chnoteblock;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import com.xxmicloxx.NoteBlockAPI.model.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2019-07-29
 */
@MSExtension("CHNoteBlock")
public class CHNoteBlock extends AbstractExtension {
    private static final Map<String, Song> songMap = new HashMap<>();

    public static boolean registerSong(String id, Song song) {
        if (songMap.get(id) != null) {
            return false;
        }
        songMap.put(id, song);
        return true;
    }

    public static Optional<Song> removeSong(String id) {
        return Optional.ofNullable(songMap.remove(id));
    }

    public static Optional<Song> getSong(String id) {
        return Optional.ofNullable(songMap.get(id));
    }

    public static boolean contains(String id) {
        return songMap.containsKey(id);
    }

    public static List<String> getIds() {
        return new ArrayList<>(songMap.keySet());
    }

    @Override
    public Version getVersion() {
        return new SimpleVersion(1, 0, 0);
    }
}
