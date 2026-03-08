package com.jose.franchiseapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "franchises")
public class Franchise {
    @Id
    private String id;
    private String name;
    private Long branchCounter = 0L;
    private Long productCounter = 0L;
    private List<Branch> branches = new ArrayList<>();
}
