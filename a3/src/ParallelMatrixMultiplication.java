package a3.src;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelMatrixMultiplication {
    public static double[] parallelMatrixMultiplication(double[]vector, double[][] matrix) throws ExecutionException, InterruptedException {

        // Create a thread pool.
        ExecutorService executor = Executors.newCachedThreadPool();

        int numRows = matrix.length;
        int numCols = matrix[0].length;         // Set the number of rows and columns.
        double[] result = new double[numRows];  // Initialize result matrix.

        int threads = 8;
        int size = numRows/threads;

        if (size == 0){
            size = 1;
        }

        List<Future<Void>> fut = new ArrayList<>();

        for (int i = 0; i < numCols; i += size){
            int start = i;
            int end;
            if (i + size <= numCols){
                end = i + size;
            } else {
                end = numCols;
            }

            Callable<Void> task = () -> {
                for(int l = start; l < end; l++){
                    for (int j = 0; j < numRows; j++){
                        result[j] += matrix[j][l] * vector[l];
                    }
                }
                return null;
            };
            fut.add(executor.submit(task));
        }

        // Use get() to wait for the result of the computation.
        for(Future<Void> future: fut){
            future.get();
        }

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    return result;
    }
}