package com.italoccosta.commercehub.service.impl;

import com.italoccosta.commercehub.dto.ProductRequest;
import com.italoccosta.commercehub.dto.StockRequest;
import com.italoccosta.commercehub.entity.Product;
import com.italoccosta.commercehub.entity.Stock;
import com.italoccosta.commercehub.exceptions.InvalidStockException;
import com.italoccosta.commercehub.exceptions.ProductNotFoundException;
import com.italoccosta.commercehub.repository.ProductRepository;
import com.italoccosta.commercehub.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product registerProduct(ProductRequest productRequest) {

        Product newProduct = this.toProductEntity(productRequest);
        productRepository.save(newProduct);
        return newProduct;

    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductById(UUID productId) {

        return findExistingProduct(productId);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(UUID productId, ProductRequest productRequest) {

        Product productToUpdate = findExistingProduct(productId);

        productToUpdate.updateProduct(productRequest.name(),
                productRequest.description(),
                productRequest.price());

        return productToUpdate;
    }

    @Override
    public void deleteProduct(UUID productId) {

        Product productToDelete = findExistingProduct(productId);
        productRepository.delete(productToDelete);

    }

    @Override
    public void registerStock(UUID productId, StockRequest stock) {

        Product product = findExistingProduct(productId);
        Stock newStock = this.toStockEntity(stock, product);

        product.addStock(newStock);

    }

    @Override
    public void increaseStock(UUID productId, Integer quantity) {

        Product product = findExistingProduct(productId);
        if(product.getStock() == null){
            throw  new InvalidStockException("This product doesn't have stock");
        }
        product.getStock().increaseQuantity(quantity);

    }

    @Override
    public void removeStock(UUID productId) {
        Product product = findExistingProduct(productId);
        product.removeStock();
    }

    private Product toProductEntity(ProductRequest productRequest) {
       return new Product(
                productRequest.name(),
                productRequest.description(),
                productRequest.price()
        );
    }

    private Stock toStockEntity(StockRequest stockRequest, Product product) {
        return new Stock(
                product,
                stockRequest.quantity()
        );
    }

    private Product findExistingProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

}
