package com.slimgears.sample;

import java.util.List;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface GenericListContainerPrototypeBuilder<T, _B extends GenericListContainerPrototypeBuilder<T, _B>> {

    _B items(List<GenericListItem<T>> items);
    
}
