
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {

    public static void main(String[] args) {
        try {
            // Locate the registry on the localhost at port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Lookup the remote object using its bound name
            Calculator stub = (Calculator) registry.lookup("CalculatorService");

            // Invoke remote methods
            int a = 20;
            int b = 5;

            System.out.println("Calling remote methods with a=" + a + " and b=" + b);
            System.out.println("Addition: " + stub.add(a, b));
            System.out.println("Subtraction: " + stub.subtract(a, b));
            System.out.println("Multiplication: " + stub.multiply(a, b));
            System.out.println("Division: " + stub.divide(a, b));

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
