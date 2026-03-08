package com.jose.franchiseapi.interfaces.controller;

import com.jose.franchiseapi.application.service.FranchiseService;
import com.jose.franchiseapi.domain.model.Branch;
import com.jose.franchiseapi.domain.model.Franchise;
import com.jose.franchiseapi.domain.model.Product;
import com.jose.franchiseapi.interfaces.dto.BranchTopProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
public class FranchiseController {
    private final FranchiseService service;
    @GetMapping
    public Flux<Franchise> getAllFranchises() {
        return service.getAllFranchises();
    }

    @PostMapping
    public Mono<Franchise> createFranchise(@RequestBody Franchise franchise) {
        return service.createFranchise(franchise.getName());
    }

    @GetMapping("/{franchiseId}/branches")
    public Flux<Branch> getBranchesByFranchise(@PathVariable String franchiseId) {

        return service.getAllBranchesByFranchise(franchiseId);
    }


    @PostMapping("/{franchiseId}/branches")
    public Mono<Franchise> addBranch(@PathVariable String franchiseId,
                                     @RequestBody Branch branch) {
        return service.addBranch(franchiseId, branch);
    }

    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    public Mono<Franchise> addProduct(
            @PathVariable String franchiseId,
            @PathVariable Long branchId,
            @RequestBody Product product) {

        return service.addProduct(franchiseId, branchId, product);
    }

    @DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public Mono<Franchise> deleteProduct(
            @PathVariable String franchiseId,
            @PathVariable Long branchId,
            @PathVariable Long productId) {

        return service.deleteProduct(franchiseId, branchId, productId);
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock/{stock}")
    public Mono<Franchise> updateStock(
            @PathVariable String franchiseId,
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @PathVariable Integer stock) {

        return service.updateStock(franchiseId, branchId, productId, stock);
    }

    @GetMapping("/{franchiseId}/top-products")
    public Flux<BranchTopProductDTO> getTopProducts(
            @PathVariable String franchiseId) {

        return service.getTopProductsByBranch(franchiseId);
    }
}
