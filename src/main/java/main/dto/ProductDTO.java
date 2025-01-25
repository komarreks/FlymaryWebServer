package main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductDTO {
    private String id;
    private String name;
    private Double price;
    private String filterNode;
    private List<String> imageUrl;
}
