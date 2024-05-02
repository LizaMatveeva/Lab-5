package ifmo.lab.server.database;


import ifmo.lab.server.models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.time.LocalDateTime;

/**
 * Class for providing database operations for Products using a LinkedHashMap.
 */
public class DataBaseProvider {
    private LinkedHashMap<Integer, Product> dataBase;
    private final String fileName;
    private LocalDateTime initializationTime;

    public DataBaseProvider(String fileName) {
        this.fileName = fileName;
        this.dataBase = loadDatabase();
        this.initializationTime = LocalDateTime.now();
    }

    public LocalDateTime getInitializationTime() {
        return initializationTime;
    }

    public LinkedHashMap<Integer, Product> getDataBase() {
        return new LinkedHashMap<>(dataBase);
    }

    private synchronized Long generateNextId() {
        return dataBase.values().stream().mapToLong(Product::getId).max().isPresent()
                ? dataBase.values().stream().mapToLong(Product::getId).max().getAsLong() + 1
                : 1;
    }

    public boolean addData(Integer key, Product product) {
        if (!dataBase.containsKey(key)) {
            product.setId(generateNextId());
            dataBase.put(key, product);
            return true;
        }
        return false;
    }

    public Product updateData(long id, Product updatedProduct) {
        updatedProduct.setId(id);
        for (Map.Entry<Integer, Product> entry : dataBase.entrySet()) {
            if (entry.getValue().getId() == id) {
                dataBase.put(entry.getKey(), updatedProduct);
                return updatedProduct;
            }
        }
        return null;
    }

    public boolean removeDataByKey(Integer key) {
        if (dataBase.containsKey(key)) {
            dataBase.remove(key);
            return true;
        }
        return false;
    }
    public void saveDatabase() {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write("Key,Id,Name,Coordinates,CreationDate,Price,PartNumber,ManufactureCost,UnitOfMeasure,Owner\n");
            for (Map.Entry<Integer, Product> entry : dataBase.entrySet()) {
                Integer key = entry.getKey();
                Product product = entry.getValue();
                writer.write(key + "," + product.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
        }
    }

    public LinkedHashMap<Integer, Product> loadDatabase() {
        LinkedHashMap<Integer, Product> loadedDatabase = new LinkedHashMap<>();
        File file = new File(fileName);

        try (Scanner scanner = new Scanner(file)) {
            // Пропускаем строку с заголовком
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int firstCommaIndex = line.indexOf(',');
                if (firstCommaIndex == -1) continue;

                Integer key = Integer.parseInt(line.substring(0, firstCommaIndex));
                String productData = line.substring(firstCommaIndex + 1);
                Product product = Product.fromCSV(productData);

                loadedDatabase.put(key, product);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return loadedDatabase;
    }



    public void clear() {
        dataBase.clear();
    }

}
