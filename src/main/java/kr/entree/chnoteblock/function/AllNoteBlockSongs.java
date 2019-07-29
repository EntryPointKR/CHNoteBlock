package kr.entree.chnoteblock.function;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREIllegalArgumentException;
import com.laytonsmith.core.exceptions.CRE.CREIndexOverflowException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import kr.entree.chnoteblock.CHNoteBlock;

/**
 * Created by JunHyeong Lim on 2019-07-30
 */
@api
public class AllNoteBlockSongs extends CHNoteBlockFunction {
    @Override
    public Class<? extends CREThrowable>[] thrown() {
        return new Class[]{
                CREIllegalArgumentException.class,
                CREIndexOverflowException.class
        };
    }

    @Override
    public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
        CArray array = new CArray(t);
        for (String id : CHNoteBlock.getIds()) {
            array.push(new CString(id, t), t);
        }
        return array;
    }

    @Override
    public String getName() {
        return "all_nbsongs";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{0};
    }

    @Override
    public String docs() {
        return "array {} Returns all of loaded noteblock songs";
    }
}
