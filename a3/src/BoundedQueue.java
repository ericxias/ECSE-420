package a3.src;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.atomic.AtomicInteger;


public class BoundedQueue<T> {
    // code is based on the Bounded queue as a linked list from the textbook, modified by using an array instead of a linked list
    T[] array;
    ReentrantLock enqLock, deqLock;
    Condition notEmptyCondition, notFullCondition;
    AtomicInteger size;
    int head, tail;
    int capacity;
   
    public BoundedQueue(int _capacity){
        capacity = _capacity;
        // instantiate array
        array = (T[]) new Object[capacity];
        head = 0;
        tail = head;
        size = new AtomicInteger(0);
        enqLock = new ReentrantLock();
        notFullCondition = enqLock.newCondition();
        deqLock = new ReentrantLock();
        notEmptyCondition = deqLock.newCondition();
    }


    public void enq(T x) throws InterruptedException {
        boolean mustWakeDequeuers = false;
        enqLock.lock();
        try {
            while (size.get() == capacity){
                notFullCondition.await();
            }
            // add x to the tail of the queue and increment tail
            array[tail] = x;
            tail = (tail + 1) % capacity;
            if (size.getAndIncrement() == 0){
                mustWakeDequeuers = true;
            }
        } finally {
            enqLock.unlock();
        }
        if (mustWakeDequeuers){
            deqLock.lock();
            try {
                notEmptyCondition.signalAll();
            } finally {
                deqLock.unlock();
            }
        }
    }


    public T deq() throws InterruptedException{
        T result;
        boolean mustWakeEnqueuers = true;
        deqLock.lock();
        try {
            while (size.get() == 0){
                notEmptyCondition.await();
            }
            // remove the head of the queue and increment head
            result = array[head];
            head = (head + 1) % capacity;
            if (size.getAndIncrement() == capacity){
                mustWakeEnqueuers = true;
            }
        } finally {
            deqLock.unlock();
        }
        if (mustWakeEnqueuers){
            enqLock.lock();
            try {
                notFullCondition.signalAll();
            } finally {
                enqLock.unlock();
            }
        }
        return result;
    }

}



