package net.andreho.struct.primitive;

/**
 * Created by 666 on 10.08.2016.
 */
public interface IntList extends IntCollection {
   /**
    * Returns the element at the specified position in this list.
    *
    * @param index index of the element to return
    * @return the element at the specified position in this list
    * @throws IndexOutOfBoundsException if the index is out of range
    *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
    */
   int get(int index);

   /**
    * Replaces the element at the specified position in this list with the
    * specified element (optional operation).
    *
    * @param index   index of the element to replace
    * @param element element to be stored at the specified position
    * @return the element previously at the specified position
    * @throws UnsupportedOperationException if the <tt>set</tt> operation
    *                                       is not supported by this list
    * @throws IllegalArgumentException      if some property of the specified
    *                                       element prevents it from being added to this list
    * @throws IndexOutOfBoundsException     if the index is out of range
    *                                       (<tt>index &lt; 0 || index &gt;= size()</tt>)
    */
   int set(int index, int element);

   /**
    * Inserts the specified element at the specified position in this list
    * (optional operation).  Shifts the element currently at that position
    * (if any) and any subsequent elements to the right (adds one to their
    * indices).
    *
    * @param index   index at which the specified element is to be inserted
    * @param element element to be inserted
    * @throws UnsupportedOperationException if the <tt>add</tt> operation
    *                                       is not supported by this list
    * @throws IllegalArgumentException      if some property of the specified
    *                                       element prevents it from being added to this list
    * @throws IndexOutOfBoundsException     if the index is out of range
    *                                       (<tt>index &lt; 0 || index &gt; size()</tt>)
    */
   void addAt(int index, int element);

   /**
    * Removes the element at the specified position in this list (optional
    * operation).  Shifts any subsequent elements to the left (subtracts one
    * from their indices).  Returns the element that was removed from the
    * list.
    *
    * @param index the index of the element to be removed
    * @return the element previously at the specified position
    * @throws UnsupportedOperationException if the <tt>remove</tt> operation
    *                                       is not supported by this list
    * @throws IndexOutOfBoundsException     if the index is out of range
    *                                       (<tt>index &lt; 0 || index &gt;= size()</tt>)
    */
   int removeAt(int index);

   /**
    * Inserts all of the elements in the specified collection into this
    * list at the specified position (optional operation).  Shifts the
    * element currently at that position (if any) and any subsequent
    * elements to the right (increases their indices).  The new elements
    * will appear in this list in the order that they are returned by the
    * specified collection's iterator.  The behavior of this operation is
    * undefined if the specified collection is modified while the
    * operation is in progress.  (Note that this will occur if the specified
    * collection is this list, and it's nonempty.)
    *
    * @param index index at which to insert the first element from the
    *              specified collection
    * @param c     collection containing elements to be added to this list
    * @return <tt>true</tt> if this list changed as a result of the call
    * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
    *                                       is not supported by this list
    * @throws NullPointerException          if the specified collection is null
    * @throws IllegalArgumentException      if some property of an element of the
    *                                       specified collection prevents it from being added to this list
    * @throws IndexOutOfBoundsException     if the index is out of range
    *                                       (<tt>index &lt; 0 || index &gt; size()</tt>)
    */
   boolean addAll(int index, IntCollection c);

   // Search Operations

   /**
    * Returns the index of the first occurrence of the specified element
    * in this list, or -1 if this list does not contain the element.
    * More formally, returns the lowest index <tt>i</tt> such that
    * <tt>(element==get(i))</tt>,
    * or -1 if there is no such index.
    *
    * @param element element to search for
    * @return the index of the first occurrence of the specified element in
    * this list, or -1 if this list does not contain the element
    */
   int indexOf(int element);

   /**
    * Returns the index of the last occurrence of the specified element
    * in this list, or -1 if this list does not contain the element.
    * More formally, returns the highest index <tt>i</tt> such that
    * <tt>(element==get(i))</tt>,
    * or -1 if there is no such index.
    *
    * @param element element to search for
    * @return the index of the last occurrence of the specified element in
    * this list, or -1 if this list does not contain the element
    */
   int lastIndexOf(int element);


}
