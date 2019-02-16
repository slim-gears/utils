package com.slimgears.util.autovalue.annotations;

public interface HasSelf<B extends HasSelf<B>> {
    default B self() {
        //noinspection unchecked
        return (B)this;
    }
}
