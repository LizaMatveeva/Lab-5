package ifmo.lab.server.controller;

import ifmo.lab.server.dao.ProductDAO;
import ifmo.lab.server.dao.ProductDAOImpl;
import ifmo.lab.server.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ProductController interface.
 */
public class ProductControllerImpl implements ProductController {
    private final ProductDAO productDAO;

    public ProductControllerImpl(String fileName) {
        this.productDAO = new ProductDAOImpl(fileName);
    }

    @Override
    public String info() {
        return productDAO.info();
    }

    @Override
    public String show() {
        return productDAO.getListAllProducts().stream()
                .map(Product::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Map<Integer, Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @Override
    public String insert(Integer key, Product product) {
        return productDAO.addProduct(key, product);
    }

    @Override
    public Product update(long id, Product product) {
        return productDAO.updateProduct(id, product);
    }

    @Override
    public String removeByKey(Integer key) {
        boolean success = productDAO.removeProductByKey(key);
        return success ? "Product removed successfully." : "Failed to remove product.";
    }

    @Override
    public String clear() {
        boolean success = productDAO.clear();
        return success ? "All products cleared." : "Failed to clear products.";
    }

    @Override
    public void save() {
        productDAO.save();
    }

    @Override
    public List<Product> filterContainsName(String name) {
        return productDAO.filterProductsByName(name);
    }

    @Override
    public List<Product> filterLessThanPrice(float price) {
        return productDAO.filterProductsByPriceLessThan(price);
    }

    @Override
    public List<String> printFieldDescendingPartNumber() {
        return productDAO.getProductsSortedByPartNumberDescending();
    }

    @Override
    public boolean removeGreaterKey(int key) {
        Map<Integer, Product> allProducts = productDAO.getAllProducts();
        boolean anyRemoved = false;
        List<Integer> keysToRemove = new ArrayList<>();

        for (Integer productKey : allProducts.keySet()) {
            if (productKey > key) {
                keysToRemove.add(productKey);
            }
        }

        for (Integer k : keysToRemove) {
            boolean removed = productDAO.removeProductByKey(k);
            if (removed) {
                anyRemoved = true;
            }
        }

        return anyRemoved;
    }
    @Override
    public boolean removeLower(Product product){
        return productDAO.removeLower(product);
    }
}
