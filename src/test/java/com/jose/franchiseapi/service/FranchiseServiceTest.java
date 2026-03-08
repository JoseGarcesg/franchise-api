package com.jose.franchiseapi.service;

import com.jose.franchiseapi.application.service.FranchiseService;
import com.jose.franchiseapi.domain.model.Branch;
import com.jose.franchiseapi.domain.model.Franchise;
import com.jose.franchiseapi.domain.model.Product;
import com.jose.franchiseapi.domain.repository.FranchiseRepository;
import com.jose.franchiseapi.interfaces.dto.BranchTopProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FranchiseServiceTest {
    private FranchiseRepository repository;
    private FranchiseService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(FranchiseRepository.class);
        service = new FranchiseService(repository);
    }

    @Test
    void shouldGetAllFranchises() {
        Franchise franchise = new Franchise();
        franchise.setName("KFC");
        when(repository.findAll()).thenReturn(Flux.just(franchise));

        StepVerifier.create(service.getAllFranchises())
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void shouldCreateFranchise() {
        Franchise franchise = new Franchise();
        franchise.setName("KFC");

        when(repository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(service.createFranchise("KFC"))
                .expectNextMatches(f -> f.getName().equals("KFC"))
                .verifyComplete();

        verify(repository).save(any(Franchise.class));
    }

    @Test
    void shouldAddBranch() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setBranches(new ArrayList<>());
        franchise.setBranchCounter(0L);

        Branch branch = new Branch();
        branch.setName("Branch 1");

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.addBranch("1", branch))
                .expectNextMatches(f -> f.getBranches().size() == 1 && f.getBranches().get(0).getId() == 1L)
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenAddingBranchToNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.addBranch("1", new Branch()))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldGetAllBranchesByFranchise() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Branch 1");
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.getAllBranchesByFranchise("1"))
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenGettingBranchesOfNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.getAllBranchesByFranchise("1"))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldAddProduct() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setProductCounter(0L);
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setProducts(new ArrayList<>());
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        Product product = new Product();
        product.setName("Product 1");

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.addProduct("1", 1L, product))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().size() == 1 
                        && f.getBranches().get(0).getProducts().get(0).getId() == 1L)
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenAddingProductToNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.addProduct("1", 1L, new Product()))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldThrowErrorWhenAddingProductToNonExistentBranch() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setBranches(new ArrayList<>());

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.addProduct("1", 1L, new Product()))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldDeleteProduct() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        Product product = new Product();
        product.setId(1L);
        List<Product> products = new ArrayList<>();
        products.add(product);
        branch.setProducts(products);
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.deleteProduct("1", 1L, 1L))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().isEmpty())
                .verifyComplete();
    }

    @Test
    void shouldDeleteProductEvenIfProductDoesNotExist() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setProducts(new ArrayList<>());
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.deleteProduct("1", 1L, 99L))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().isEmpty())
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenDeletingProductFromNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteProduct("1", 1L, 1L))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldThrowErrorWhenDeletingProductFromNonExistentBranch() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setBranches(new ArrayList<>());

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.deleteProduct("1", 1L, 1L))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldUpdateStock() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        List<Product> products = new ArrayList<>();
        products.add(product);
        branch.setProducts(products);
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateStock("1", 1L, 1L, 20))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().get(0).getStock() == 20)
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenUpdatingStockInNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.updateStock("1", 1L, 1L, 20))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldThrowErrorWhenUpdatingStockInNonExistentBranch() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setBranches(new ArrayList<>());

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateStock("1", 1L, 1L, 20))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldThrowErrorWhenUpdatingStockOfNonExistentProduct() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setProducts(new ArrayList<>());
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateStock("1", 1L, 1L, 20))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldGetTopProductsByBranch() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Branch 1");
        
        Product p1 = new Product();
        p1.setName("P1");
        p1.setStock(10);
        
        Product p2 = new Product();
        p2.setName("P2");
        p2.setStock(20);
        
        List<Product> products = new ArrayList<>();
        products.add(p1);
        products.add(p2);
        branch.setProducts(products);
        
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.getTopProductsByBranch("1"))
                .expectNextMatches((BranchTopProductDTO dto) -> dto.getProductName().equals("P2") && dto.getStock() == 20)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenGettingTopProductsOfNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.getTopProductsByBranch("1"))
                .verifyComplete();
    }

    @Test
    void shouldUpdateFranchiseName() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Old Name");

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateFranchiseName("1", "New Name"))
                .expectNextMatches(f -> f.getName().equals("New Name"))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenUpdatingNameOfNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.updateFranchiseName("1", "Nuevo"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldUpdateBranchName() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Old Branch Name");
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateBranchName("1", 1L, "New Branch Name"))
                .expectNextMatches(f -> f.getBranches().get(0).getName().equals("New Branch Name"))
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenUpdatingBranchNameInNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.updateBranchName("1", 1L, "New Name"))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void shouldUpdateProductName() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        Branch branch = new Branch();
        branch.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setName("Old Product Name");
        List<Product> products = new ArrayList<>();
        products.add(product);
        branch.setProducts(products);
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);
        franchise.setBranches(branches);

        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.updateProductName("1", 1L, 1L, "New Product Name"))
                .expectNextMatches(f -> f.getBranches().get(0).getProducts().get(0).getName().equals("New Product Name"))
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenUpdatingProductNameInNonExistentFranchise() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.updateProductName("1", 1L, 1L, "New Name"))
                .expectError(ResponseStatusException.class)
                .verify();
    }
}
