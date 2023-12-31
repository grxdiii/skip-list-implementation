package SkipList;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.Random;
import java.util.Iterator;
import java.util.Collection;
/**
 * This class implements the skip list data structured using Java's sorted set interface. The list accepts
 * generic types as parameters by comparing each element and storing them in ascending order. Elements are
 * instances of the wrapper class SkipListSetItem, which contains the location of its previous, next, below,
 * and above element. Each element also contain the value of the specified type.
 * @author Gradi Tshielekeja Mbuyi
 * @version 1.0
 * @since July 27, 2023
 * @param <T> accepts generics as parameters.
 */
public class SkipListSet <T extends  Comparable<T>> implements SortedSet<T> {
    private SkipListSetItem <T> head;
    private SkipListSetItem <T> bottomHead;
    private SkipListSetItem <T> bottomTail;
    private Integer height;
    private Integer size;
    private Random random;

    /**
     * Default constructor to initialize a skip list. This constructor does not accept any parameters, however as we
     * initialize the list, an instance of the SkipListSetItem class is created and stored in the head variable.
     * The constructor also create an instance for the random variable.
     */
    public SkipListSet() {
        head = new SkipListSetItem<>();
        random = new Random(1);
        size = 0;
        head.setLevel(1);
        height = 1;
        bottomHead = head;
    }

    /**
     * Secondary constructor to initialize a skip list. This constructor accepts a collection as a parameter,
     * retrieving each of its values and adding them to the skip list.
     * @param collection collection whose value is to be stored in a skip list set.
     */
    public SkipListSet(Collection<? extends T> collection) {
        head = new SkipListSetItem<>();
        random = new Random(1);
        size = 0;
        head.setLevel(1);
        height = 1;
        bottomHead = head;
        addAll(collection);
    }

    /**
     * This method creates and returns an instance of the SkipListSetIterator class. Through the SkipListSetIterator
     * object, we can check to see if a list has a next value. If a value is present, we can retrieve and remove it
     * from the skip list.
     * @return Returns a new instance of the SkipListSetIterator class.
     */
    @Override
    public Iterator<T> iterator() {
        return new SkipListSetIterator<>(this);
    }

    /**
     * This method returns the value of the head element within a skip list.
     * @return Returns value of the first element within a skip list.
     */
    @Override
    public T first() {
        return bottomHead.getValue();
    }

    /**
     * This method returns the value of the tail element within a skip list.
     * @return Returns value of the last element within a skip list.
     */
    @Override
    public T last() {
        return bottomTail.getValue();
    }

    /**
     * This method is used by the class SkipLIstSetIterator to retrieve the head element of a skip list.
     * @return Returns first element in the skip list.
     */
    public SkipListSetItem<T> getHead() {
        return bottomHead;
    }

    /**
     * Getter method to retrieve the size of a skip list.
     * @return Returns the size (cardinality) of a skip list.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * This method is used check if a skip list is empty - returning true if this the case, otherwise returns false.
     * @return Returns true if the skip list has no element, otherwise return false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * This method is unsupported per Project instructions
     * @param fromElement low endpoint (inclusive) of the returned set
     * @param toElement high endpoint (exclusive) of the returned set
     * @return Fails to return
     * @throws UnsupportedOperationException as contracted
     */
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is unsupported per Project instructions
     * @param toElement high endpoint (exclusive) of the returned set
     * @return Fails to return
     * @throws UnsupportedOperationException as contracted
     */
    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is unsupported per Project instructions
     * @param fromElement low endpoint (inclusive) of the returned set
     * @return Fails to return
     * @throws UnsupportedOperationException as contracted
     */
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is unsupported per Project instructions
     * @return Returns null
     */
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * This method checks to see if a given object or value is contained within a skip list. It calls the search()
     * method, returning true if the value is present in the list, or false if the value isn't present in the list.
     * @param object element whose presence in this set is to be tested.
     * @return Returns true if element is present in the set, otherwise returns false.
     */
    @Override
    public boolean contains(Object object) {
        SkipListSetItem<T> current = search((T) object);
        return current.compareTo((T) object) == 0;
    }

    /**
     * This method checks to see if the elements of a given collection are contained within a skip list. It calls the
     * contains() method n times, where n represents the number of value within the given collection, returning false
     * if a specific element isn't present in the list, or otherwise returning true. The performance of this operation
     * isn't ideal given the fact that collections are not always sorted.
     * @param collection collection to be checked for containment in this set.
     * @return Returns true if all elements in collection is contained in the skip list.
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        for(Object item : collection) {
            if(!contains(item)) return false;
        }
        return true;
    }

    /** This method is used to clear the skip list. Each value is reset to its original state. */
    @Override
    public void clear() {
        head = new SkipListSetItem<>();
        size = 0;
        head.setLevel(1);
        height = 1;
        bottomHead = head;

        System.gc();
    }

    /**
     * This method perform search operations. Given a specified value, search() attempts to find the closet value
     * less than or equal to the given object. It's important to note that this is an internal method, meaning
     * that it isn't accessible beyond the scope of the SkipListSet class.
     * @param value element whose presence in this set is to be tested.
     * @return Returns the location of the closest element to the given value.
     */
    private SkipListSetItem<T> search(T value) {
        SkipListSetItem<T> current = head;

        while(current.getBelow() != null) {
            current = current.getBelow();
            while(current.getNext() != null && current.getNext().compareTo(value) <= 0) {
                current = current.getNext();
            }
        }

        return current;
    }

    /**
     * This method is used to add elements of a given collections to a skip list, utilizing the add() method to perform
     * this operation.
     * @param collection collection containing elements to be added to this set.
     * @return Returns true once each element is added to the list.
     */
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        for(T item : collection) {
            add(item);
        }
        return true;
    }

    /**
     * This method is used to remove elements of a given collections from a skip list, utilizing the remove() method
     * to perform this operation.
     * @param collection collection containing elements to be removed from this set
     * @return Always returns true after completion of removal.
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        for(Object item : collection) {
            remove(item);
        }
        return true;
    }

    /**
     * This method is used to retain similar elements between a collection and a skip list. All the elements that are
     * present in the skip list but aren't present in the collection are removed.
     * @param collection collection containing elements to be retained in this set
     * @return Always returns true after completion of the retaining operations.
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        SkipListSetItem<T> current = bottomHead;
        SkipListSet<T> newSkipList = new SkipListSet<>();

        for(int i = 0; i < size; i++) {
            if(collection.contains(current.getValue())) {
                newSkipList.add(current.getValue());
            }
        }

        head = newSkipList.head;
        bottomHead = newSkipList.bottomHead;
        size = newSkipList.size;
        bottomTail = newSkipList.bottomTail;
        random = newSkipList.random;
        height = newSkipList.height;

        System.gc();

        return true;
    }

    /**
     * This method allocates space for an array with a size of SkipListSet.size. As we iterate over the entire
     * skip list, each element is stored within the array. The array is then returned to the user.
     * @return Returns an array containing every element within a skip list.
     */
    @Override
    public Object[] toArray() {
        Object[] object = new Object[size];
        SkipListSetItem<T> current = bottomHead;

        for(int i = 0; i < size; i++) {
            object[i] = current.getValue();
            current = current.getNext();
        }

        return object;
    }

    /**
     * This method allocates space for an array with a size of SkipListSet.size. As we iterate over the entire
     * skip list, each element is stored within the array. The array is then returned to the user.
     * @param array the array into which the elements of this set are to be stored, if it is big enough;
     * otherwise, a new array of the same runtime type is allocated for this purpose.
     * @return Returns an array containing every element within a skip list.
     * @param <T1> the method accepts generics types
     */
    @Override
    public <T1> T1[] toArray(T1[] array) {
        for(T1 value : array) {
            add((T) value);
        }

        T1[] newArray = (T1[]) new Object[size];
        SkipListSetItem<T> current = bottomHead;

        for(int i = 0; i < size; i++) {
            newArray[i] = (T1) current.getValue();
            current = current.getNext();
        }

        return newArray;
    }

    /**
     * This internal method is used to change the head element of a skip list. In the case where a given value is
     * less than the head, changeHead() is called and through it, the head of the skip list is updated.
     * @param headValue value to be added before the head.
     * @param numLevelToAdd numbers of times the current head will be present in the list.
     */
    private void changeHead(T headValue, int numLevelToAdd) {
        T newValue = head.getValue();
        SkipListSetItem<T> currentHead = bottomHead;
        SkipListSetItem<T> newNode;
        SkipListSetItem<T> bellow = null;

        if(numLevelToAdd == height) {
            numLevelToAdd--;
        }

        /* Updates value of our current head and new node */
        while(currentHead != null) {
            if(numLevelToAdd != 0) {
                newNode = new SkipListSetItem<>(newValue);
                newNode.setLevel(currentHead.getLevel());
                newNode.setBelow(bellow);
                newNode.setPrevious(currentHead);
                newNode.setNext(currentHead.getNext());

                if(currentHead.getNext() != null) {
                    currentHead.getNext().setPrevious(newNode);
                }

                currentHead.setNext(newNode);
                bellow = newNode;
                newNode = newNode.getAbove();
                numLevelToAdd--;
            }

            currentHead.setValue(headValue);
            currentHead = currentHead.getAbove();
        }
    }

    /**
     * This internal method is used to add the new element numLevelToAdd times in a skip list.
     * @param newNode element to be added in the skip list.
     * @param numLevelToAdd numbers of times the new node will be present in the list.
     */
    private void addNewLevel(SkipListSetItem<T> newNode, int numLevelToAdd) {
        SkipListSetItem<T> previous = newNode.getPrevious();

        if(numLevelToAdd == 0) {
            return ;
        }

        for(int i = 0; i < numLevelToAdd; i++) {
            newNode.setAbove(new SkipListSetItem<>(newNode.getValue()));
            newNode.getAbove().setLevel(newNode.getLevel() + 1);
            newNode.getAbove().setBelow(newNode);
            if(height == newNode.getLevel() + 1) {
                head.setAbove(new SkipListSetItem<>(head.getValue()));
                head.getAbove().setBelow(head);
                head.getAbove().setLevel(head.getLevel() + 1);
                head = head.getAbove();
                height++;
            }
            while(previous.getAbove() == null) {
                previous = previous.getPrevious();
            }
            newNode.getAbove().setPrevious(previous.getAbove());
            newNode.getAbove().setNext(previous.getAbove().getNext());
            if(previous.getAbove().getNext() != null) {
                previous.getAbove().getNext().setPrevious(newNode.getAbove());
            }
            previous.getAbove().setNext(newNode.getAbove());
            newNode = newNode.getAbove();
            previous = previous.getAbove();
        }
    }

    /**
     * This method adds specified element in a skip list, depending on whether the element is already contained in
     * the list. This method does not allow duplicates. Before performing the add operation, add() calls search() and
     * checks to see if the element is within the list.
     * @param value element whose presence in this collection is to be ensured.
     * @return Returns true if value is successfully added to the skip list, and false if value is already in the list.
     */
    @Override
    public boolean add(T value) {
        SkipListSetItem<T> newNode = null;
        SkipListSetItem<T> current = search(value);

        int numLevelToAdd = 0;

        while(random.nextBoolean()) {
            numLevelToAdd++;
        }

        if(isEmpty()) {
            head.setValue(value);
            head.setAbove(new SkipListSetItem<>(value));
            head.getAbove().setBelow(head);
            head.getAbove().setLevel(head.getLevel() + 1);
            head = head.getAbove();
            size++;
            height++;
            return true;
        }

        if(current.compareTo(value) == 0) {
            return false;

        } else if(current.compareTo(head.getValue()) == 0 && current.compareTo(value) > 0) {
            changeHead(value, numLevelToAdd);

        } else {
            newNode = new SkipListSetItem<>(value);
            newNode.setPrevious(current);
            newNode.setNext(current.getNext());
            if(current.getNext() != null) {
                current.getNext().setPrevious(newNode);
            } else {
                bottomTail = newNode;
            }
            current.setNext(newNode);
            addNewLevel(newNode, numLevelToAdd);
        }

        size++;
        return true;
    }

    /**
     * This method removes specified element in a skip list, depending on whether the element is contained in
     * the list. If the value is present, it is removed and the method returns true, otherwise the method returns false.
     * @param value object to be removed from this set, if present.
     * @return Returns true if element is successfully removed, otherwise returns false.
     */
    @Override
    public boolean remove(Object value) {
        SkipListSetItem<T> current = search((T) value);

        if(current.compareTo((T) value) != 0) {
            return false;

        } else if(current.compareTo(head.getValue()) == 0) {
            SkipListSetItem<T> next = current.getNext();

            while(current != null) {
                current.setValue(next.getValue());
                if(current.getNext() != null && current.getNext().compareTo(next.getValue()) == 0) {
                    current.setNext(current.getNext().getNext());
                    current.getNext().setPrevious(current);
                }
                current = current.getAbove();
            }

        } else while(current != null) {
            current.getPrevious().setNext(current.getNext());
            if(current.getNext() != null) {
                current.getNext().setPrevious(current.getPrevious());
            }
            current = current.getAbove();
        }

        size--;
        return true;
    }

    /**
     * This method remove single node from the skip. This differs from the remove method because during its operation,
     * the method removes the element from single level, rather than removing the element all together.
     * @param current node to be removed from the list
     * @return Returns the next node from the one removed from the list.
     */
    private SkipListSetItem<T> deleteSingleNode(SkipListSetItem<T> current) {
       SkipListSetItem<T> next = current.getNext();

        if(current.getNext() != null) current.getNext().setPrevious(current.getPrevious());
        if(current.getBelow() != null) current.getBelow().setAbove(null);
        if(current.getPrevious() != null) current.getPrevious().setNext(current.getNext());

        current = null;

        return next;
    }

    /**
     * Method to balance the skip list to improve the performance of the search operations - it's important to
     * note that this method does not achieve this successfully.
     */
    public void reBalance() {
        int levelValue, nodePosition = 0;
        int maxLevel = (int) (Math.log(size) / Math.log(2));

        SkipListSetItem<T> current = bottomHead;
        SkipListSetItem<T> currentTop;

        if(height > maxLevel) {
            nodePosition++;
            currentTop = head;
            for(int i = 0; i < height - maxLevel; i++) {
                SkipListSetItem<T> next = currentTop.getNext();
                while(next != null) {
                    next = deleteSingleNode(next);
                }
                currentTop = currentTop.getBelow();
                currentTop.setAbove(null);
            }
            head = currentTop;
            current = current.getNext();
        }

        while(current != null) {
            nodePosition++;
            levelValue = (int) (maxLevel - (Math.log(nodePosition) / Math.log(2)));
            currentTop = current;
            while(currentTop.getAbove() != null) {
                currentTop = currentTop.getAbove();
            }
            if(currentTop.getLevel() < levelValue) {
                addNewLevel(current, levelValue);
            }
            current = current.getNext();
        }
    }
}