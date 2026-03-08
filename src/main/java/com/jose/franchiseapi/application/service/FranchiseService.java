package com.jose.franchiseapi.application.service;

import com.jose.franchiseapi.domain.model.Franchise;
import com.jose.franchiseapi.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FranchiseService {
    private final FranchiseRepository repository;

    public Mono<Franchise> createFranchise(String name) {

        Franchise franchise = new Franchise();
        franchise.setName(name);
        franchise.setBranches(new ArrayList<>());

        return repository.save(franchise);
    }
}
