package main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProductDTO {
    private String id;
    private String name;
    private Double price;
    private Double count;
    private String filterNode;
    private List<String> imageUrl;

    private List<CharacTDO> characTDOs;

    public ProductDTO() {
        characTDOs = new ArrayList<>();
    }
}
