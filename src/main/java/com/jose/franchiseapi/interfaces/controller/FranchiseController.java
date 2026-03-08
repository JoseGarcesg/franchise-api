package com.jose.franchiseapi.interfaces.controller;

import com.jose.franchiseapi.application.service.FranchiseService;
import com.jose.franchiseapi.domain.model.Branch;
import com.jose.franchiseapi.domain.model.Franchise;
import com.jose.franchiseapi.domain.model.Product;
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

    @PostMapping("/{franchiseId}/branches/{branchName}/products")
    public Mono<Franchise> addProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchName,
            @RequestBody Product product) {

        return service.addProduct(franchiseId, branchName, product);
    }
}
