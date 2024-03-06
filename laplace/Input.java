package laplace;

import java.util.Scanner;

public class Input implements InputInterface {
	// Creaza un obiect de tip Scanner pentru citirea de la tastatura
    private Scanner scanner = new Scanner(System.in);

    @Override
    public String getInputPath() {
        System.out.println("Introduceti calea catre fisierul sursa: ");
        return scanner.next();
    }

    @Override
    public String getOutputPathNegative() {
        System.out.println("Introduceti calea catre fisierul destinatie (Laplace Negative): ");
        return scanner.next();
    }

    @Override
    public String getOutputPathPositive() {
        System.out.println("Introduceti calea catre fisierul destinatie (Laplace Positive): ");
        return scanner.next();
    }
    
}

