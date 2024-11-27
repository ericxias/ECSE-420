package a3.src;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineList<T> {
    // add and remove methods are based on the pseudocode from the textbook
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
        // instantiate variables and key
        Node pred = null, curr = null;
        int key = item.hashCode();
        // lock head, iterate through list by locking and unlocking current node to find key
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
                    // contains item
                    return true;
                }
                // does not contain item
                return false;
            } finally {
                // unlock rest of nodes
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public static void main(String[] args){
        FineList<Integer> fineList = new FineList<>();
        System.out.println("Testing FineList contains method:");
        
        // add elements to list
        System.out.println("Adding 1, 2, 3 to the list");
        fineList.add(1);
        fineList.add(2);
        fineList.add(3);

        // True cases: i = 1 - 3 False case: i = 4
        for (int i = 1; i <= 4; i++){
            System.out.println("Testing if " + i + " is in the list: " + fineList.contains(i));
            // fail conditions
            if ((!fineList.contains(i) && i != 4) || (fineList.contains(i) && i == 4)){
                System.out.println("Test failed");
            }
        }

        // remove element from list and test if contains returns false
        System.out.println("Removing 1 from the list");
        fineList.remove(1);
        System.out.println("Testing if 1 is in the list: " + fineList.contains(1));
        // fail condition
        if (fineList.contains(1)){
            System.out.println("Test failed");
        }
        
        // test edge cases max and min integer values
        System.out.println("Adding max and min values to the list");
        fineList.add(Integer.MAX_VALUE);
        fineList.add(Integer.MIN_VALUE);
        System.out.println("Testing if max value is in the list: " + fineList.contains(Integer.MAX_VALUE));
        System.out.println("Testing if min value is in the list: " + fineList.contains(Integer.MIN_VALUE));
        // fail conditions
        if (!fineList.contains(Integer.MAX_VALUE) || !fineList.contains(Integer.MIN_VALUE)){
            System.out.println("Test failed");
        }

    }
}
