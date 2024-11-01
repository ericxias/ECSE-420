package a2.src;

import java.util.concurrent.locks.*;
import java.util.concurrent.TimeUnit;

class Filter implements Lock{
    int[] level;
    int[] victim;
    private int n;

    public Filter(int n){
        level = new int[n];
        victim = new int[n]; 
        for (int i = 0; i < n; i++){
            level[i] = 0;
        }
    }

    @Override
    public void lock(){
        int i = (int) Thread.currentThread().getId() % 5;
        System.out.println("Thread " + i + " acquiring lock");
        for (int L = 1; L < n; L++){
            level[i] = L;
            victim[L] = i;
            // spin while conflicts exist
            for (int k = 0; k < n; k++){
                while (k != i && level[k] >= L && victim[L] == i);
            }
        }
    }

    @Override
    public void unlock(){
        int i = (int) Thread.currentThread().getId() % 5;
        System.out.println("Thread " + i + " releasing lock");
        level[i] = 0;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


}