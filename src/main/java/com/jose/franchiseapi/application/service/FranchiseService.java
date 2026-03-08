package com.jose.franchiseapi.application.service;

import com.jose.franchiseapi.domain.model.Branch;
import com.jose.franchiseapi.domain.model.Franchise;
import com.jose.franchiseapi.domain.model.Product;
import com.jose.franchiseapi.domain.repository.FranchiseRepository;
import com.jose.franchiseapi.interfaces.dto.BranchTopProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FranchiseService {
    private final FranchiseRepository repository;

    public Flux<Franchise> getAllFranchises() {
        return repository.findAll();
    }

    public Mono<Franchise> createFranchise(String name) {

        Franchise franchise = new Franchise();
        franchise.setName(name);

        return repository.save(franchise);
    }

    public Mono<Franchise> addBranch(String franchiseId, Branch branch) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {
                    Long newId = franchise.getBranchCounter()+1;
                    franchise.setBranchCounter(newId);
                    branch.setId(newId);
                    franchise.getBranches().add(branch);
                    return repository.save(franchise);
                });
    }

    public Flux<Branch> getAllBranchesByFranchise(String franchiseId) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()));
    }

    public Mono<Franchise> addProduct(String franchiseId, Long branchId, Product product) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {

                    Branch branch = franchise.getBranches()
                            .stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));
                    Long newId = franchise.getProductCounter()+1;
                    franchise.setProductCounter(newId);
                    product.setId(newId);
                    branch.getProducts().add(product);
                    return repository.save(franchise);
                });
    }

    public Mono<Franchise> deleteProduct(String franchiseId, Long branchId, Long productId) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {

                    Branch branch = franchise.getBranches()
                            .stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    branch.getProducts()
                            .removeIf(p -> p.getId().equals(productId));

                    return repository.save(franchise);
                });
    }

    public Mono<Franchise> updateStock(String franchiseId, Long branchId, Long productId, Integer stock) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {

                    Branch branch = franchise.getBranches()
                            .stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    Product product = branch.getProducts()
                            .stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    product.setStock(stock);

                    return repository.save(franchise);
                });
    }

    public Flux<BranchTopProductDTO> getTopProductsByBranch(String franchiseId) {

        return repository.findById(franchiseId)
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch ->

                        Flux.fromIterable(branch.getProducts())
                                .reduce((p1, p2) ->
                                        p1.getStock() >= p2.getStock() ? p1 : p2
                                )
                                .map(product -> new BranchTopProductDTO(
                                        branch.getName(),
                                        product.getName(),
                                        product.getStock()
                                ))
                );
    }

    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return repository.save(franchise);
                });
    }

    public Mono<Franchise> updateBranchName(String franchiseId, Long branchId, String newName) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {

                    franchise.getBranches()
                            .stream()
                            .filter(branch -> branch.getId().equals(branchId))
                            .findFirst()
                            .ifPresent(branch -> branch.setName(newName));

                    return repository.save(franchise);
                });
    }
}
