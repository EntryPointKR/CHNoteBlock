package kr.entree.chnoteblock.function;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREIllegalArgumentException;
import com.laytonsmith.core.exceptions.CRE.CRELengthException;
import com.laytonsmith.core.exceptions.CRE.CRENotFoundException;
import com.laytonsmith.core.exceptions.CRE.CREPlayerOfflineException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.EntitySongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.NoteBlockSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.RangeSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import kr.entree.chnoteblock.CHNoteBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by JunHyeong Lim on 2019-07-29
 */
@api
public class PlayNoteBlockSong extends AbstractFunction {
    @Override
    public Class<? extends CREThrowable>[] thrown() {
        return new Class[]{
                CREPlayerOfflineException.class,
                CRELengthException.class,
                CRECastException.class,
                CRENotFoundException.class
        };
    }

    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public Boolean runAsync() {
        return null;
    }

    public static void setupPlayer(SongPlayer songPlayer, CArray data, Target t) {
        if (data.containsKey("players")) {
            CArray players = Static.getArray(data.get("players", t), t);
            for (Mixed p : players) {
                MCPlayer player = Static.GetPlayer(p, t);
                songPlayer.addPlayer((Player) player.getHandle());
            }
        }
    }

    public static void setupPlayer(RangeSongPlayer player, CArray data, Target t) {
        setupPlayer((SongPlayer) player, data, t);
        if (data.containsKey("distance")) {
            double distance = Static.getNumber(data.get("distance", t), t);
            player.setDistance((int) distance);
        }
    }

    public static void setupPlayer(PositionSongPlayer player, CArray data, Target t) {
        setupPlayer((RangeSongPlayer) player, data, t);
        if (data.containsKey("location")) {
            MCLocation location = ObjectGenerator.GetGenerator().location(data.get("location", t), null, t);
            player.setTargetLocation((Location) location.getHandle());
        }
    }

    public static void setupPlayer(NoteBlockSongPlayer player, CArray data, Target t) {
        setupPlayer((RangeSongPlayer) player, data, t);
        if (data.containsKey("block")) {
            MCLocation location = ObjectGenerator.GetGenerator().location(data.get("block", t), null, t);
            player.setNoteBlock((Block) location.getBlock().getHandle());
        }
    }

    public static void setupPlayer(EntitySongPlayer player, CArray data, Target t) {
        setupPlayer((RangeSongPlayer) player, data, t);
        if (data.containsKey("entity")) {
            MCEntity entity = Static.getEntity(data.get("entity", t), t);
            player.setEntity((Entity) entity.getHandle());
        }
    }

    @Override
    public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
        MCPlayer player = args.length >= 2
                ? Static.GetPlayer(args[0], t)
                : env.getEnv(CommandHelperEnvironment.class).GetPlayer();
        CArray playData = Static.getArray(args[1], t);
        String id = playData.get("id", t).val();
        String type = playData.containsKey("type")
                ? playData.get("type", t).val()
                : "radio";
        Song song = CHNoteBlock.getSong(id).orElseThrow(() ->
                new CRENotFoundException("Could not find the song of the given id " + id, t));
        SongPlayer songPlayer = null;
        switch (type) {
            case "radio": {
                RadioSongPlayer radioPlayer = new RadioSongPlayer(song);
                setupPlayer(radioPlayer, playData, t);
                songPlayer = radioPlayer;
                break;
            }
            case "position": {
                PositionSongPlayer positionPlayer = new PositionSongPlayer(song);
                setupPlayer(positionPlayer, playData, t);
                songPlayer = positionPlayer;
                break;
            }
            case "noteblock": {
                NoteBlockSongPlayer noteBlockPlayer = new NoteBlockSongPlayer(song);
                setupPlayer(noteBlockPlayer, playData, t);
                songPlayer = noteBlockPlayer;
                break;
            }
            case "entity": {
                EntitySongPlayer entityPlayer = new EntitySongPlayer(song);
                setupPlayer(entityPlayer, playData, t);
                songPlayer = entityPlayer;
                break;
            }
        }
        if (songPlayer != null) {
            if (player != null) {
                songPlayer.addPlayer((Player) player.getHandle());
            }
            songPlayer.setPlaying(true);
            songPlayer.setAutoDestroy(true);
        } else {
            throw new CREIllegalArgumentException("Unknown song player type " + type, t);
        }
        return CVoid.VOID;
    }

    @Override
    public Version since() {
        return MSVersion.LATEST;
    }

    @Override
    public String getName() {
        return "play_nbsong";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{2};
    }

    @Override
    public String docs() {
        return "void {player, playData}";
    }
}
