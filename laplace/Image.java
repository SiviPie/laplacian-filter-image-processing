package laplace;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// Clasa abstracta Image extinde clasa ImageParent
abstract class Image extends ImageParent {
    // Bloc de initializare non-static
    {
        System.out.println("A fost creat un obiect Image.");
    }
	
	// Variabile pentru citirea din fisier si scrierea in fisier
    private FileInputStream fin = null;

    // Array pentru continutul fisierului imagine
    protected byte file_content[];

    // Array pentru stocarea datelor imagine
    protected byte data_matrix[][];
    
    // Array pentru rezultatul transformarii Laplace
    protected byte laplacian[];
    
    // Variabile pentru masurarea timpului
    private long start_time = 0;
    private long end_time = 0;
    
    // Variabile pentru dimensiuni si offset
    private long offset ;
    private long width;
    private long height;
    
    // Getters
    public long getOffset() {
        return offset;
    }
    
    public long getWidth() {
        return width;
    }
    
    public long getHeight() {
        return height;
    }
    
    // Setters
    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    // Metoda pentru citirea octetilor din fisierul sursa
    private void ReadImageBytes() throws IOException{
    	// Inregistram timpul inceperii executiei
        start_time = System.currentTimeMillis();
        
        try {
        	// Cream un FileInputStream pentru citirea din fisier
            fin = new FileInputStream(getFile());
            // Preluam dimensiunea fisierului citit
            file_content = new byte[(int)getFile().length()];
            // Citim continutul fisierului si il punem in array-ul corespunzator
            fin.read(file_content);
        } catch(FileNotFoundException e) {
            System.out.println("Nu a fost gasit fisierul : " + e);
        } catch(IOException e) {
            System.out.println("Exceptie la citirea fisierului : " + e);
        } finally {
        	if (fin != null) {
        		// Inchidem FileInputStream
                fin.close();
                // Inregistram timpul incheierii executiei
                end_time = System.currentTimeMillis();
            }
        }
        // Afisam timpul de executie in consola
        System.out.println("Timp citire imagine: " + (end_time - start_time) + "ms");
    } 

    // Constructor
    public Image(String path) throws IOException{
    	// Apelam constructorul clasei parinte
        super(path);
        
        // Citim datele imaginii
        ReadImageBytes();

        // Initializam variabilele pentru dimensiuni
        setOffset(Convertor.byteToLong(file_content, 10, 4));
        setWidth(Convertor.byteToLong(file_content, 18, 4));
        setHeight(Convertor.byteToLong(file_content, 22, 4));

        // Initializam array-urile pentru rezultatul transformarii laplace si pentru datele imaginii
        laplacian = new byte[(int)getFile().length()];
        data_matrix = new byte[(int)getHeight()][(int)getWidth()*3];
        
        // Copiem continutul imaginii in array-ul ce va fi folosit la prelucrare
        /*
        for (int i = 0; i < getOffset(); i++){
            laplacian[i] = file_content[i];
        }
        */
        for (int i = 0; i < file_content.length; i++) {
            laplacian[i] = file_content[i];
        }


        // Populam data_matrix cu datele imaginii
        for (int i = 0; i < (int)getHeight(); i++){
            for (int j = 0; j < 3 * (int)getWidth(); j++){
                data_matrix[i][j] = file_content[(int)(getOffset() + 3 * i * getWidth() + j)];
            }
        }
    }

    // Metode abstracte pentru transformarile Laplace
    public abstract void LaplacePositive() throws IOException;
    public abstract void LaplaceNegative() throws IOException;
}
