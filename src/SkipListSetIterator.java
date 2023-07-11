import java.util.Iterator;

public class SkipListSetIterator <T extends Comparable<T>> implements Iterator<T>{

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        return null;
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }
}