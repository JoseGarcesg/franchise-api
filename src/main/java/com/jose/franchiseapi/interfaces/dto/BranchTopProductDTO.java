package com.jose.franchiseapi.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchTopProductDTO {
    private String branchName;
    private String productName;
    private Integer stock;
}
