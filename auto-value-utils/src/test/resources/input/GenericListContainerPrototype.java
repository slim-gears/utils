package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.util.List;

@AutoValuePrototype
@UseMetaDataExtension
public interface GenericListContainerPrototype<T> {
    List<com.slimgears.sample.GenericListItem<T>> items();
}
