package main.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BasketDTO {
    private List<BasketString> products;
    private Double totalCost;
    private Double total;

    public void setProducts(List<BasketString> products) {
        this.products = products;
        updateTotal();
    }

    public BasketDTO(){
        products = new ArrayList<>();
        totalCost = 0.0;
        total = 0.0;
    }

    private void updateTotal(){
        BigDecimal totalCost = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);

        for (BasketString product : products) {
            totalCost.add(product.getPrice());
            total.add(product.getTotal());
        }

        this.totalCost = totalCost.doubleValue();
        this.total = total.doubleValue();
    }
}
