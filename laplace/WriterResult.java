package laplace;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;

public class WriterResult implements Runnable {
	// Declaram un obiect de tip PipedInputStream
    private PipedInputStream in;
    // Declaram un String pentru calea catre fisierul de iesire
    String output_file;
    // Declaram un int pentru a specifica tipul transformarii Laplace (1 = Negative, 2 = Positive)
    int laplace_type;

    // Constructor
    public WriterResult(PipedInputStream in, String output_file, int laplace_type) {
        this.in = in;
        this.output_file = output_file;
        this.laplace_type = laplace_type;
    }

    // Metoda care va fi executata in firul de executie al clasei
    @Override
    public void run() {
        try (FileOutputStream fos = new FileOutputStream(output_file)) {
        	// Inregistram timpul la care am inceput executia
        	float start_time = System.currentTimeMillis();
        	// Cream un buffer pentru a citi tin pipe
        	byte[] buffer = new byte[1024];
        	// Variabila pentru a retine numarul de octeti cititi
        	int bytes_read;
            
        	// Cat timp exista octeti de citit din pipe
            while ((bytes_read = in.read(buffer)) != -1) {
            	// Scriem octetii in FileOutputStream pentru a-i salva in fisierul de iesire
                fos.write(buffer, 0, bytes_read);
                
                System.out.println("WriterResult: bucata de segment primita");
                Thread.sleep(10);
            }
            // Inregistram timpul la care am terminat executia
            float end_time = System.currentTimeMillis();
            // Afisam un mesaj in consola
            System.out.println("WriterResult: Segmente " + (laplace_type == 1 ? "Negative" : "Positive") + " primite si scrise in fisierul destinatie " + output_file + ". Timp de executie: " + (end_time - start_time));
              
        } catch (IOException | InterruptedException e ) {
            e.printStackTrace();
        }
    }
}
