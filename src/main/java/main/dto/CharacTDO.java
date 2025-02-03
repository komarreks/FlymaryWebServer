package main.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CharacTDO {
    private String id;
    private String productId;
    private String name;
    private List<String> imageUrl;
    private Double price;
}
