package com.codevepa.onlinebookstore.controller;

import com.codevepa.onlinebookstore.model.Product;
import com.codevepa.onlinebookstore.service.ProductService;
import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController()
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping()
        public List<Product> getAllProducts() {
            return service.findAll();
    }

    @GetMapping("/{prodId}")
    public ResponseEntity<?> getProductById(@PathVariable int prodId) {
        try{
            return new ResponseEntity<>(service.findById(prodId), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try{
            Product newProduct = service.addProduct(product);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/{productId}", consumes = "application/json")
    public ResponseEntity<?> updateProduct(@PathVariable int productId, @RequestBody Product product) {

        Product newProduct;
        try{
            newProduct = service.updateProduct(productId, product);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (newProduct != null) {
            return new ResponseEntity<>(newProduct, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId) {
        Product product = service.findById(productId);
        if (product != null) {
            service.deleteProduct(productId);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword) {
        List<Product> products = service.searchProduct(keyword);
        return ResponseEntity.ok(products);
    }

}
