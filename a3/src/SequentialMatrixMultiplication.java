package a3.src;

public class SequentialMatrixMultiplication {
    
    /**
     * 
     * @param vector Vector array input.
     * @param matrix Matrix array input.
     * @return The resulting vector output.
     */
    public static double[] sequentialMatrixMultiplication(double [] vector, double[][] matrix){
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        double[] result = new double[numRows];

        for (int j = 0; j < numCols; j++){
            for (int i = 0; i < numRows; i++){
                result[i] += matrix[i][j] * vector[j];
            }
        }

        return result;
    }
}
