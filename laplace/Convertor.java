package laplace;

// Clasa utila pentru conversii
public class Convertor {
	
	// Metoda pentru convertirea unui array de bytes la un long
    public static long byteToLong(byte array[], int offset, int length){
    	// Variabila pentru rezultatul conversiei
        long result = 0;

        // Parcurgerea inversa a array-ului pentru a construi numarul long
        for (int i = length - 1; i >= 0; i--){
        	// Adaugam la rezultat octetul curent, deplasat la pozitia corecta in numarul long
        	result = result | ((array[offset+i] & 0xFF) << (i*8)) & 0xffffffffL;
        }
        return result;
    }
    
}
