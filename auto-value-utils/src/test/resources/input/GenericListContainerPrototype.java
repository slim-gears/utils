package com.slimgears.sample;

import com.slimgears.sample.GenericListItem;
import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.util.List;

@AutoValuePrototype
@UseMetaDataExtension
public interface GenericListContainerPrototype<T> {
    List<GenericListItem<T>> items();
}
