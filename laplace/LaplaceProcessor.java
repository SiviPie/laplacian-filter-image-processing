package laplace;

public class LaplaceProcessor {
    private Laplace laplace;

    // Getter
    public synchronized Laplace getLaplace() {
        return laplace;
    }

    // Setter
    public synchronized void setLaplace(Laplace laplace) {
        this.laplace = laplace;
    }
}