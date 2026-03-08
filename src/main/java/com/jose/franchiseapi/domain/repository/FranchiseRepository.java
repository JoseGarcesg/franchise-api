package com.jose.franchiseapi.domain.repository;

import com.jose.franchiseapi.domain.model.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {
}
