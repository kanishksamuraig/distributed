
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorServer {

    public static void main(String[] args) {
        try {
            // Create an instance of the implementation class
            CalculatorImpl calculator = new CalculatorImpl();

            // Start the RMI registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the remote object to the registry with the name "CalculatorService"
            registry.rebind("CalculatorService", calculator);

            System.out.println("Calculator Server is ready and running...");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
