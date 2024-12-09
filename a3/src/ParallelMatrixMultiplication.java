package a3.src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Parallel implementation of matrix-vector multiplication using the Future interface.
 * 
 * Inspired by the implementation in Chapter 16 of Herlihy and Shavit, with modifications that suit the algorithm used in SequentialMatrixMultiplication.java.
 */

public class ParallelMatrixMultiplication {

    // Create a cached thread pool.
    static ExecutorService exec = Executors.newCachedThreadPool();
    int threads = 8;
    static int threshold = 50;
    
        /**
         * 
         * @param vector Vector input.
         * @param matrix Matrix input.
         * @return The resulting vector output.
         * @throws IllegalArgumentException
         */
        public static double[] parallelMatrixMultiplication(double[] vector, double[][] matrix) throws IllegalArgumentException{
            int numRows = matrix.length;
            int numCols = matrix[0].length;
            double[] result = new double[numRows];
    
            try{
                // Submit the task and wait for it to return.
                Future<?> mulTask = exec.submit(new MatrixVectorMultiplicationTask(vector, matrix, 0, numRows, result));
                try{
                    mulTask.get();
                } catch(Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                exec.shutdown();
                exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                
            } catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
    
            return result;
        }
    
        static class MatrixVectorMultiplicationTask implements Runnable{
            double[] vector;
            double[][] matrix;
            int numRows, numCols, startRow, size;                           // Set the number of rows and columns.
            double[] result;                                          // Initialize result matrix.
    
            MatrixVectorMultiplicationTask(double[] vector, double[][] matrix, int startRow, int numRows, double[] result){
                this.vector = vector;
                this.matrix = matrix;
                this.startRow = startRow;
                this.numRows = numRows;
                this.result = result;
            }
    
            @Override
            public void run() {
                try {
                    numCols = matrix[0].length;

                    // Once the threshold is reached, it is faster to calculate the results sequentially.
                    if(numRows <= threshold){
                    for(int l = 0; l < numCols; l++){
                        result[startRow] += matrix[startRow][l] * vector[l];
                    }
                } else {
                    // Otherwise, split the array in half recursively and submit the subtasks.
                    int middleRow = startRow + (numRows/2);
                    int endRow = numRows-(middleRow-startRow);
                    Future<?> subTask1 = exec.submit(new MatrixVectorMultiplicationTask(vector, matrix, startRow, numRows/2, result));
                    Future<?> subTask2 = exec.submit(new MatrixVectorMultiplicationTask(vector, matrix, middleRow, endRow, result));

                    // Use get() to wait for the output of the calculation.
                    subTask1.get();
                    subTask2.get();
                }

            } catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        
    }
}