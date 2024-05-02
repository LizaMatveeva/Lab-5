package ifmo.lab;


import ifmo.lab.client.ConsoleUI;
import ifmo.lab.server.builders.ProductBuilder;
import ifmo.lab.server.commands.Invoker;
import ifmo.lab.server.exceptions.FileException;

public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String databaseFilename = args[0];
                ConsoleUI session = new ConsoleUI(new Invoker(databaseFilename));
                session.start();
            } else {
                System.out.println("Необходимо ввести название файла базы данных при запуске программы.");
            }
        } catch (FileException e) {
            System.out.println(e.getMessage());
        }
    }
}