package main.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BasketString {
    private String productId;
    //private ProductDTO product;
    private String characId;
    //private CharacTDO charac;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal total;
}
