package a2.src;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class LamportBakeryLock implements Lock {
    private int n;
    private volatile Label[] label;
    private volatile boolean[] flag;

    public LamportBakeryLock(int n){
        this.n = n;
        label = new Label[n];
        flag = new boolean[n];

        for (int i = 0; i<n; i++){
            flag[i] = false;
            label[i] = new Label();
        }
    }

    @Override
    public void lock() {
        int me = (int) Thread.currentThread().getId() % n;
        flag[me] = true;
        int max = Label.maximum(label);
        label[me] = new Label(max + 1);

        for (int k = 0;k < n;k++){
            while (k != me && flag [k] && (label[me].compareTo(label[k]) < 0)){

            }
        }
    }
    @Override
    public void lockInterruptibly() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lockInterruptibly'");
    }
    @Override
    public boolean tryLock() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryLock'");
    }
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryLock'");
    }
    @Override
    public void unlock() {
        flag[(int) Thread.currentThread().getId() % n] = false;
    }
    @Override
    public Condition newCondition() {
        return null;
    }

    static class Label implements Comparable<Label>{
        int counter;
        int id;

        Label(){
            counter = 0;
            id = (int) Thread.currentThread().getId();
        }

        Label(int x){
            counter = x;
            id = (int) Thread.currentThread().getId();
        }

        static int maximum(Label[] labels){
            int max = 0;
            for(Label label: labels){
                if(label.counter > max){
                    max = label.counter;
                }
            }
            return max;
        }

        @Override
        public int compareTo(LamportBakeryLock.Label o) {
            if(this.counter < o.counter || (this.counter == o.counter && this.id < o.id)){
                return -1;
            } else if (this.counter > o.counter){
                return 1;
            } else {
                return 0;
            }
        }
    }
}
