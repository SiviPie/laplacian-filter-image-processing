package laplace;

import java.io.IOException;

// Clasa Laplace extinde clasa Image
public class Laplace extends Image {
	// Numarul de segmente in care vom imparti imaginea
	private static final int NUM_SEGMENTS = 4;
	
	// Constructor
    public Laplace(String path) throws IOException{
        super(path);
    }
    
    public int getSegmentSize() {
        // Calculam si returnam dimensiunea fiecarui segment
        return (int) (laplacian.length / NUM_SEGMENTS);
    }

    // Metoda pentru a obtine un segment anume al array-ului laplacian
    public byte[] getSegment(int segmentIndex) {
        // Calculam indicele de start si de final al segmentului
        int index_start = segmentIndex * getSegmentSize();
        int index_end = (segmentIndex + 1) * getSegmentSize();

        // Extragem segmentul
        byte[] segment = new byte[index_end - index_start];
        System.arraycopy(laplacian, index_start, segment, 0, segment.length);

        return segment;
    }
    
    // Kernel-ul este de forma [0, ker_2, 0; ker_2, ker_1, ker_2; 0, ker_2, 0]
    public void LaplaceTransform(int... ker) throws IOException {

        // Cream o matrice ajutatoare pentru calcul
        long auxMatrix[][] = new long[(int) getHeight()][3 * (int) getWidth()];

        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < 3 * getWidth(); j++)
                auxMatrix[i][j] = data_matrix[i][j];
        }
        // Aplicam transformarea Laplace pentru fiecare pixel din imagine
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < 3 * getWidth(); j += 3) {
                // Aplicam filtrul (matricea de convolutie), tratand si cazurile in care ne aflam in margine
                auxMatrix[i][j] = ker[0] * data_matrix[i][j]
                        + ((i > 0) ? ker[1] * data_matrix[i - 1][j] : 0)
                        + ((i < getHeight() - 1) ? ker[1] * data_matrix[i + 1][j] : 0)
                        + ((j > 0) ? ker[1] * data_matrix[i][j - 3] : 0)
                        + ((j < 3 * getWidth() - 3) ? ker[1] * data_matrix[i][j + 3] : 0);
                auxMatrix[i][j + 1] = ker[0] * data_matrix[i][j + 1]
                        + ((i > 0) ? ker[1] * data_matrix[i - 1][j + 1] : 0)
                        + ((i < getHeight() - 1) ? ker[1] * data_matrix[i + 1][j + 1] : 0)
                        + ((j > 0) ? ker[1] * data_matrix[i][j - 2] : 0)
                        + ((j < 3 * getWidth() - 4) ? ker[1] * data_matrix[i][j + 4] : 0);
                auxMatrix[i][j + 2] = ker[0] * data_matrix[i][j + 2]
                        + ((i > 0) ? ker[1] * data_matrix[i - 1][j + 2] : 0)
                        + ((i < getHeight() - 1) ? ker[1] * data_matrix[i + 1][j + 2] : 0)
                        + ((j > 0) ? ker[1] * data_matrix[i][j - 1] : 0)
                        + ((j < 3 * getWidth() - 5) ? ker[1] * data_matrix[i][j + 5] : 0);

                // Limitam valorile obtinute pentru a se incadra in intervalul [0, 255]
                auxMatrix[i][j] = (byte) Math.min(255, Math.max(0, auxMatrix[i][j]));
                auxMatrix[i][j + 1] = (byte) Math.min(255, Math.max(0, auxMatrix[i][j + 1]));
                auxMatrix[i][j + 2] = (byte) Math.min(255, Math.max(0, auxMatrix[i][j + 2]));             
            }
        }

        // Copiem rezultatul in array-ul laplacian
        for (int i = 0; i < getHeight(); i++)
            for (int j = 0; j < 3 * getWidth(); j++)
                //laplacian[(int) (getOffset() + 3 * i * getWidth() + j)] = (byte)(auxMatrix[i][j] / 8);
            	laplacian[(int) (getOffset() + 3 * i * getWidth() + j)] = (byte) auxMatrix[i][j];


    }

    // Kernel = [ 0 -1 0 ; -1 4 -1; 0 -1 0]
    public void LaplaceNegative() throws IOException {
    	LaplaceTransform(4, -1);
    }
    
    // Kernel = [ 0 1 0 ; 1 -4 1; 0 1 0]
    public void LaplacePositive() throws IOException {
    	LaplaceTransform(-4, 1);
    }
    
    // Returnam rezultatul transformarii Laplace
    public byte[] getProcessedData() {
        return laplacian;
    }
}
