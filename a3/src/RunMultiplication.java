package a3.src;

import java.util.Random;

public class RunMultiplication {

    public static double[] newRandomVector(int length) {
        Random generator = new Random();
        double[] createdVector = new double[length];
        for (int i = 0; i < length; i++){
            createdVector[i] = (double)generator.nextInt(11);
        }
        //printVector(createdVector);
        return createdVector;
    }

    public static double[][] newRandomMatrix(int length) {
        Random generator = new Random();
        double[][] createdMatrix = new double[length][length];
        for (int i = 0; i < length; i++){
            for(int j = 0; j < length; j++){
                createdMatrix[i][j] = (double)generator.nextInt(11);
            }
        }
        //printMatrix(createdMatrix);
        return createdMatrix;
    }

    /**
	 * Prints out every value of a given vector.
	 * @param a is the vector input
	 */
	public static void printVector(double[] a){
		for (int i=0; i<a.length; i++){
			System.out.print(a[i] + " ");
			}
			System.out.println();
		System.err.println("\n");
	}


    /**
	 * Prints out every value of a given matrix.
	 * @param a is the matrix input
	 */
	public static void printMatrix(double[][] a){
		for (int i=0; i<a.length; i++){
			for (int j=0; j<a.length; j++){
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
		System.err.println("\n");
	}

    public static void main(String[] args) throws IllegalArgumentException{
        int length = 4000;
        double[] vector = newRandomVector(length);
        double[][] matrix = newRandomMatrix(length);

        double[] sequentialResult;
        double[] parallelResult;

        long seqStartTime = System.currentTimeMillis();
        sequentialResult = SequentialMatrixMultiplication.sequentialMatrixMultiplication(vector, matrix);
        long seqEndTime = System.currentTimeMillis();
        long sequentialTime = seqEndTime - seqStartTime;
        System.out.println("Sequential matrix muliplication runtime: " + (sequentialTime) + " ms");

        //printVector(sequentialResult);

        long parStartTime = System.currentTimeMillis();
        parallelResult = ParallelMatrixMultiplication.parallelMatrixMultiplication(vector, matrix);
        long parEndTime = System.currentTimeMillis();
        long parallelTime = parEndTime - parStartTime;
        System.out.println("Parallel matrix muliplication runtime: " + (parallelTime) + " ms");

        //printVector(parallelResult);
    }
    
}
