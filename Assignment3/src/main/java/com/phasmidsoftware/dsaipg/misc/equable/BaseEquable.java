/*
 * Copyright (c) 2017-2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.misc.equable;

import java.util.Objects;

public abstract class BaseEquable {

    protected abstract Equable getEquable();

    @Override
    public int hashCode() {
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return Objects.hash(getEquable().elements.toArray(new Object[getEquable().elements.size()]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEquable equable = (BaseEquable) o;
        return getEquable().equals(equable.getEquable());
    }

}