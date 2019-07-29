package kr.entree.chnoteblock.function;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import kr.entree.chnoteblock.CHNoteBlock;

import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2019-07-30
 */
@api
public class UnloadNoteBlockSong extends CHNoteBlockFunction {
    @Override
    public Class<? extends CREThrowable>[] thrown() {
        return new Class[0];
    }

    @Override
    public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
        String id = args[0].val();
        Optional<Song> removedOpt = CHNoteBlock.removeSong(id);
        return CBoolean.get(removedOpt.isPresent());
    }

    @Override
    public String getName() {
        return "unload_nbsong";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{1};
    }

    @Override
    public String docs() {
        return "bool {id} Unload the song of the given id.";
    }
}
