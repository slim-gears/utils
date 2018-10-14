package com.slimgears.apt.data;

import com.slimgears.util.generic.ScopedInstance;

import javax.lang.model.type.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeParameterScope {
    private static final TypeParameterScope empty = new TypeParameterScope(null) {
        @Override
        public boolean exists(TypeVariable type) {
            return false;
        }

        @Override
        public boolean addIfNotExists(TypeVariable type) {
            throw new IllegalStateException("Cannot add to empty scope");
        }
    };

    private static final ScopedInstance<TypeParameterScope> instance = ScopedInstance.create(empty);
    private final Map<String, TypeVariable> variableMap = new HashMap<>();
    private final TypeParameterScope parent;

    public TypeParameterScope() {
        this(current());
    }

    protected TypeParameterScope(TypeParameterScope parent) {
        this.parent = parent;
    }

    public boolean exists(TypeVariable type) {
        String key = type.asElement().getSimpleName().toString();
        return variableMap.containsKey(key) || parent.exists(type);
    }

    public boolean addIfNotExists(TypeVariable type) {
        if (exists(type)) {
            return false;
        }
        String key = type.asElement().getSimpleName().toString();
        variableMap.put(key, type);
        return true;
    }

    public static TypeParameterScope current() {
        return instance.current();
    }

    public static ScopedInstance.Closable scope() {
        return instance.scope(new TypeParameterScope());
    }
}
