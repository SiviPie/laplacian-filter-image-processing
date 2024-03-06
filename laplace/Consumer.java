package laplace;

import java.io.PipedOutputStream;

public class Consumer implements Runnable {
	// Obiect ce va tine obiectul Laplace impartit de Producer si Consumer
	private final LaplaceProcessor processor;
    // Obiect PipedOutputStream prin care Consumer va comunica cu WriterResult
    private final PipedOutputStream out;
    // Intreg prin care specificam tipul transformarii (1 = Negative, 2 = Positive)
    private final int laplace_type;

    // Constructor prin care initializam obiectele definite anterior
    public Consumer(LaplaceProcessor processor, PipedOutputStream out, int laplace_type) {
    	this.processor = processor;
        this.out = out;
        this.laplace_type = laplace_type;
    }

    // Metoda run ce va fi executata la pornirea firului de executie
    @Override
    public void run() {
        try {
        	// Definim un obiect Laplace
            Laplace laplace;
            
            // Folosim wait pentru a astepta notificare de la Producator pana cand obiectul Laplace este gata
            synchronized (processor) {
                while (processor.getLaplace() == null) {
                    processor.wait();
                }
                laplace = processor.getLaplace();
            }
            // Inregistram timpul la care am inceput executia
        	float start_time = System.currentTimeMillis();
        	
            // Procesam imaginea in functie de tipul specificat (1 = Negative, 2 = Positive)
            if (laplace_type == 1) {
                laplace.LaplaceNegative();
            } else if (laplace_type == 2) {
                laplace.LaplacePositive();
            }
            
            // Preluam datele procesate in forma de array de bytes din obiectul Laplace
            byte[] processedData = laplace.getProcessedData();

            // Segmentam datele in 4 si trimitem segmentele pe rand
            int segmentSize = processedData.length / 4;
            
            for (int i = 0; i < 4; i++) {
            	// Indexul de inceput al segmentului curent in array-ul processedData
                int start = i * segmentSize;
                
                // Indexul de sfarsit al segmentului curent in array-ul processedData
                // Daca este ultimul segment, se foloseste intreaga lungime a array-ului processedData
                int end = (i == 3) ? processedData.length : (start + segmentSize);
                
                // Scriem datele in pipe pentru a fi preluate de WriterResult
                out.write(processedData, start, end - start);
                
                // Afisam un mesaj in consola
                System.out.println("Consumer: Segment " + (laplace_type == 1? "Negative" : "Positive") + " trimis " + (i + 1));
                
                Thread.sleep(1000);
            }
            
            // Inregistram timpul la care am terminat executia
            float end_time = System.currentTimeMillis();
            
            System.out.println("Consumer " + (laplace_type == 1? "Negative" : "Positive") +  ": Timp de executie: " + (end_time - start_time));
            // Inchidem PipedOutputStream
            out.close();
        } catch (Exception e) {
        	// Afisam orice exceptie ce apare in timpul executiei
            e.printStackTrace();
        }
    }
}
