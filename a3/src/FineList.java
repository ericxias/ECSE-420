import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineList<T> {
    private Node head;

    private class Node{
        T item;
        int key;
        Node next;
        Lock lock = new ReentrantLock();

        Node(T item){
            this.item = item;
            this.key = item.hashCode();
        }

        Node(int key){
            this.key = key;
        }

        void lock(){
            lock.lock();
        }

        void unlock(){
            lock.unlock();
        }
    }

    public FineList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    public boolean add(T item){
        int key = item.hashCode();
        head.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key){
                    return false;
                }
                Node newNode = new Node(item);
                newNode.next = curr;
                pred.next = newNode;
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public boolean remove(T item){
        Node pred = null, curr = null;
        int key = item.hashCode();
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    pred.next = curr.next;
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public boolean contains(T item){
        Node pred = null, curr = null;
        int key = item.hashCode();
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key){
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key){
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    
}
