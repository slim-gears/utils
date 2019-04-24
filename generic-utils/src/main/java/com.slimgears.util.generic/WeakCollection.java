package com.slimgears.util.generic;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"NullableProblems", "SimplifyStreamApiCallChains"})
public class WeakCollection<T> implements Collection<T> {
    private final Collection<Reference<T>> collection;
    private final ReferenceQueue<T> referenceQueue = new ReferenceQueue<>();

    private WeakCollection(Collection<Reference<T>> collection) {
        this.collection = collection;
    }

    private WeakCollection() {
        this(new ArrayList<>());
    }

    public static <T> WeakCollection<T> create() {
        return new WeakCollection<>();
    }

    public static <T> WeakCollection<T> create(Collection<Reference<T>> collection) {
        return new WeakCollection<>(collection);
    }

    @Override
    public int size() {
        purge();
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        purge();
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return stream().anyMatch(o::equals);
    }

    @Override
    public Iterator<T> iterator() {
        List<T> objects = stream().collect(Collectors.toList());
        Iterator<T> objIterator = objects.iterator();
        Iterator<Reference<T>> refIterator = collection.iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return objIterator.hasNext();
            }

            @Override
            public T next() {
                refIterator.next();
                return objIterator.next();
            }

            @Override
            public void remove() {
                refIterator.remove();
                objIterator.remove();
            }
        };
    }

    @Override
    public Object[] toArray() {
        return stream().toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T1> T1[] toArray(T1[] a) {
        Object[] array = toArray();
        T1[] typedArray = Arrays.copyOf(a, array.length);
        for (int i = 0; i < array.length; ++i) {
            typedArray[i] = (T1)array[i];
        }
        return typedArray;
    }

    @Override
    public boolean add(T item) {
        purge();
        return collection.add(new WeakReference<>(item));
    }

    @Override
    public boolean remove(Object o) {
        purge();
        return collection.removeIf(ref -> Optional.ofNullable(ref.get()).map(o::equals).orElse(false));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return collection.stream()
                .map(this::add)
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return removeIf(collection::contains);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return removeIf(item -> !collection.contains(item));
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public Stream<T> stream() {
        purge();
        return collection.stream()
                .map(Reference::get)
                .filter(Objects::nonNull);
    }

    private void purge() {
        Reference<? extends T> ref;
        while ((ref = referenceQueue.poll()) != null) {
            collection.remove(ref);
        }
    }
}
