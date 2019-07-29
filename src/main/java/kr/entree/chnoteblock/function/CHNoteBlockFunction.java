package kr.entree.chnoteblock.function;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.functions.AbstractFunction;

/**
 * Created by JunHyeong Lim on 2019-07-29
 */
public abstract class CHNoteBlockFunction extends AbstractFunction {
    @Override
    public boolean isRestricted() {
        return true;
    }

    @Override
    public Version since() {
        return MSVersion.LATEST;
    }

    @Override
    public Boolean runAsync() {
        return Boolean.FALSE;
    }
}
