package laplace;

public interface InputInterface {
	// Functie pentru citirea fisierului sursa
	public String getInputPath();
	
	// Functie pentru citirea fisierului destinatie Laplace Negative
	public String getOutputPathNegative();
	
	// Functie pentru citirea fisierului destinatie Laplace Positive
	public String getOutputPathPositive();
	
}
