package a3.src;

public class SequentialMatrixMultiplication {
    
    /**
     * 
     * @param vector
     * @param matrix An
     * @return
     */
    public static double[] sequentialMatrixMultiplication(double [] vector, double[][] matrix){
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        double[] result = new double[numRows];     // Result has same number of rows as A

        for (int j = 0; j < numCols; j++){
            for (int i = 0; i < numRows; i++){
                //System.out.println("matrix [" + i + "][" + j + "]: " + matrix[i][j] + ", vector[" + j + "]: " + vector[j]);
                result[i] += matrix[i][j] * vector[j];
            }
        }

        return result;
    }
}
