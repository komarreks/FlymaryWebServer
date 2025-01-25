package main.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
public class ComplexProductData implements Serializable {
    private List<NodeDTO> filter;
    private List<ProductDTO> products;

    public ComplexProductData() {
        this.filter = new ArrayList<>();
        this.products = new ArrayList<>();
    }
}
