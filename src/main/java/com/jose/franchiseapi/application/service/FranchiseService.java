package com.jose.franchiseapi.application.service;

import com.jose.franchiseapi.domain.model.Branch;
import com.jose.franchiseapi.domain.model.Franchise;
import com.jose.franchiseapi.domain.model.Product;
import com.jose.franchiseapi.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

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

                    franchise.getBranches().add(branch);

                    return repository.save(franchise);
                });
    }

    public Flux<Branch> getAllBranchesByFranchise(String franchiseId) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()));
    }

    public Mono<Franchise> addProduct(String franchiseId, String branchName, Product product) {

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMap(franchise -> {

                    Branch branch = franchise.getBranches()
                            .stream()
                            .filter(b -> b.getName().equalsIgnoreCase(branchName))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Branch not found"));

                    branch.getProducts().add(product);

                    return repository.save(franchise);
                });
    }


}
