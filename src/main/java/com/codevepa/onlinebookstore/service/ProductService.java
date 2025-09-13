package com.codevepa.onlinebookstore.service;

import com.codevepa.onlinebookstore.model.Product;
import com.codevepa.onlinebookstore.repository.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepo repo;

    public ProductService(ProductRepo repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findById(int prodId) {

        return repo.findById(prodId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + prodId));
    }

    public Product addProduct(Product product){

        return repo.save(product);
    }

    public Product updateProduct(int productId, Product updatedProduct) {
        Product existingProduct = repo.findById(productId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId));
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductDescription(updatedProduct.getProductDescription());
        existingProduct.setProductPrice(updatedProduct.getProductPrice());
        existingProduct.setProductDate(updatedProduct.getProductDate());
        return repo.save(updatedProduct);
    }


    public void deleteProduct(int productId) {
        repo.deleteById(productId);
    }

    public List<Product> searchProduct(String keyword) {
        return repo.searchProduct(keyword);
    }

}
