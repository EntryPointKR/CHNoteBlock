package kr.entree.chnoteblock.function;

import com.laytonsmith.annotations.api;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREIOException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import kr.entree.chnoteblock.CHNoteBlock;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by JunHyeong Lim on 2019-07-29
 */
@api
public class LoadNoteBlockSong extends CHNoteBlockFunction {
    @Override
    public Class<? extends CREThrowable>[] thrown() {
        return new Class[]{
                CRECastException.class,
                CREIOException.class
        };
    }

    @Override
    public Boolean runAsync() {
        return true;
    }

    @Override
    public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
        CommandHelperPlugin plugin = CommandHelperPlugin.self;
        String id = args[0].val();
        String path = args[1].val();
        File file = new File(plugin.getDataFolder(), path);
        CClosure success = args.length >= 3
                ? Static.getObject(args[2], t, CClosure.class)
                : null;
        CClosure fail = args.length >= 4
                ? Static.getObject(args[3], t, CClosure.class)
                : null;
        if (!file.isFile()) {
            throw new CREIOException("Not exist file " + path, t);
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Song song = NBSDecoder.parse(new FileInputStream(file));
                if (song != null) {
                    boolean result = CHNoteBlock.registerSong(id, song);
                    if (success != null) {
                        success.executeCallable();
                    }
                }
            } catch (Exception ex) {
                if (fail != null) {
                    ConfigRuntimeException exception = new CREIOException(ex.getMessage(), t, ex);
                    CArray exArray = ObjectGenerator.GetGenerator().exception(exception, env, t);
                    fail.executeCallable(exArray);
                }
            }
        });
        return CVoid.VOID;
    }

    @Override
    public String getName() {
        return "load_nbsong_async";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{2, 3, 4};
    }

    @Override
    public String docs() {
        return "void {id, path[, successCallback, failCallback]} Load a song";
    }
}
