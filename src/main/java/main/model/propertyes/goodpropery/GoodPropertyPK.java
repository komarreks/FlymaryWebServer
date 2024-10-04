package main.model.propertyes.goodpropery;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import main.model.goods.Product;

@Embeddable
@NoArgsConstructor
public class GoodPropertyPK {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String property;

    public GoodPropertyPK(Product product, String property){
        this.product = product;
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GoodPropertyPK that = (GoodPropertyPK) o;

        return this.product.getId().equals(that.product.getId()) && property.equals(that.property);
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37).append(product.getId()).append(property).toHashCode();
    }


}
