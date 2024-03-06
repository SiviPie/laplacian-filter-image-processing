package laplace;

import java.io.File;

public class ImageParent {
	// Variabile ce reprezinta fisierul asociat imaginii
    private File file=null;

    // Bloc de initializare static
    static {
        System.out.println("A fost incarcata in memorie clasa ImageParent.");
    }
    
    // Constructor
    public ImageParent(String path){
        file=new File(path);
    }

    // Getters
    public File getFile() {
        return file;
    }

    // Setters
    public void setFile(File file) {
        this.file = file;
    }

}
