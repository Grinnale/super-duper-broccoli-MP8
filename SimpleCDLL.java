import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Simple doubly-linked lists.
 *
 * These do *not* (yet) support the Fail Fast policy.
 */
public class SimpleCDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The front of the list
   */
  Node2<T> front;

  /**
   * The number of values in the list.
   */
  int size;

  /**
   * A number that tells us how many changes have been made
   */
  int listID;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public SimpleCDLL() {
    Node2<T> dummy = new Node2<T>(null);
    dummy.setPrev(dummy);
    dummy.setNext(dummy);
    this.front = dummy;
    this.size = 0;
    this.listID = 0;
  } // SimpleDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;

      /**
       * The cursor is between neighboring values, so we start links
       * to the previous and next value..
       */
      Node2<T> prev = SimpleCDLL.this.front;
      Node2<T> next = SimpleCDLL.this.front.next;

      /**
       * The node to be updated by remove or set.  Has a value of
       * null when there nullis no such value.
       */
      Node2<T> update = null;

      /**
       * A number that tells us how many changes this iterator thinks have been made
       */
      int iteratorID = SimpleCDLL.this.listID;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      public void add(T val) throws UnsupportedOperationException {
        this.failFastCheck();
        this.prev = this.prev.insertAfter(val);
        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCDLL.this.size;

        // Update the position.  (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;

        this.incrementID();
      } // add(T)

      public boolean hasNext() {
        this.failFastCheck();
        return (this.pos < SimpleCDLL.this.size);
      } // hasNext()

      public boolean hasPrevious() {
        this.failFastCheck();
        return (this.pos > 0);
      } // hasPrevious()

      public T next() {
        this.failFastCheck();
        if (!this.hasNext()) {
         throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;
        // And return the value
        return this.update.value;
      } // next()

      public int nextIndex() {
        this.failFastCheck();
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        this.failFastCheck();
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        this.failFastCheck();
        if (!this.hasPrevious()){
          throw new NoSuchElementException();
        }
        // Identify the node to update
        this.update = this.prev;
        // Advance the cursor
        this.next = this.prev;
        this.prev = this.prev.prev;
        // Note the movement
        --this.pos;

        
        // And return the value
        return this.update.value;
      } // previous()

      public void remove() {
        this.failFastCheck();
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.next == this.update) {
          this.next = this.update.next;
        } // if
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        } // if

        // Do the real work
        this.update.remove();
        --SimpleCDLL.this.size;
        this.incrementID();

        // Note that no more updates are possible
        this.update = null;
      } // remove()

      public void set(T val) {
        this.failFastCheck();
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;
        // Note that no more updates are possible
        this.update = null;
      } // set(T)

      public void incrementID(){
        ++SimpleCDLL.this.listID;
        ++this.iteratorID;
      }

      public void failFastCheck(){
        if(SimpleCDLL.this.listID != this.iteratorID){
          throw new ConcurrentModificationException();
        }
      }
    };
  } // listIterator()

} // class SimpleDLL<T>
