package laplace;

//import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    // Calea catre fisierul de intrare
    private final String input_path;
    // Obiect ce va tine obiectul Laplace impartit de Producer si Consumer
    private final LaplaceProcessor processor;
    
    // Intreg prin care specificam tipul transformarii (1 = Negative, 2 = Positive)
    private final int laplace_type;

    // Constructor prin care initializam obiectele definite anterior
    public Producer(String input_path, LaplaceProcessor processor, int laplace_type) {
        this.input_path = input_path;
        this.processor = processor;
        this.laplace_type = laplace_type;
    }

    // Metoda run ce va fi executata la pornirea firului de executie
    @Override
    public void run() {
        try {
        	// Inregistram timpul la care am inceput executia
        	float start_time = System.currentTimeMillis();
        	
        	// Obiect laplace pe baza caii catre fisierul de intrare
        	Laplace laplace = new Laplace(input_path);

            // Inregistram timpul la care am terminat executia
            float end_time = System.currentTimeMillis();
            
            // Afisam timpul de executie
            System.out.println("Producer: Timp obtinere obiect Laplace " + (laplace_type == 1 ? "Negative: " : "Positive: ") + (end_time - start_time) + "ms");
            Thread.sleep(1000);
            // Folosim notify pentru a informa consumatorul ca obiectul Laplace este gata
            synchronized (processor) {
                processor.setLaplace(laplace);
                System.out.println("Producer: Obiectul Laplace " + (laplace_type == 1 ? "Negative: " : "Positive: ") + " este gata, notific Consumer-ul");
                processor.notify();
            }
        } catch (Exception e) {
        	// Afisam orice exceptie ce apare in timpul executiei
            e.printStackTrace();
        }
    }
}

