package uk.co.benjiweber.expressions.collections;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface ForwardingList<T> {
    List<T> delegate();

    default int size() {
        return delegate().size();
    }

    default Spliterator<T> spliterator() {
        return delegate().spliterator();
    }

    default ListIterator<T> listIterator() {
        return delegate().listIterator();
    }

    default List<T> subList(int fromIndex, int toIndex) {
        return delegate().subList(fromIndex, toIndex);
    }

    default boolean removeAll(Collection<?> c) {
        return delegate().removeAll(c);
    }

    default int lastIndexOf(Object o) {
        return delegate().lastIndexOf(o);
    }

    default T remove(int index) {
        return delegate().remove(index);
    }

    default Object[] toArray() {
        return delegate().toArray();
    }

    default void replaceAll(UnaryOperator<T> operator) {
        delegate().replaceAll(operator);
    }

    default T set(int index, T element) {
        return delegate().set(index, element);
    }

    default boolean add(T s) {
        return delegate().add(s);
    }

    default int indexOf(Object o) {
        return delegate().indexOf(o);
    }

    default Iterator<T> iterator() {
        return delegate().iterator();
    }

    default boolean containsAll(Collection<?> c) {
        return delegate().containsAll(c);
    }

    default void clear() {
        delegate().clear();
    }

    default boolean retainAll(Collection<?> c) {
        return delegate().retainAll(c);
    }

    default boolean remove(Object o) {
        return delegate().remove(o);
    }

    default boolean removeIf(Predicate<? super T> filter) {
        return delegate().removeIf(filter);
    }

    default ListIterator<T> listIterator(int index) {
        return delegate().listIterator(index);
    }

    default void sort(Comparator<? super T> c) {
        delegate().sort(c);
    }

    default boolean addAll(int index, Collection<? extends T> c) {
        return delegate().addAll(index, c);
    }

    default boolean isEmpty() {
        return delegate().isEmpty();
    }

    default void forEach(Consumer<? super T> action) {
        delegate().forEach(action);
    }

    default <T> T[] toArray(T[] a) {
        return delegate().toArray(a);
    }

    default Stream<T> parallelStream() {
        return delegate().parallelStream();
    }

    default T get(int index) {
        return delegate().get(index);
    }

    default void add(int index, T element) {
        delegate().add(index, element);
    }

    default Stream<T> stream() {
        return delegate().stream();
    }

    default boolean contains(Object o) {
        return delegate().contains(o);
    }

    default boolean addAll(Collection<? extends T> c) {
        return delegate().addAll(c);
    }
}
