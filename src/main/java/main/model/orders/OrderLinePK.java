package main.model.orders;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class OrderLinePK implements Serializable {
    private int lineNumber;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderLinePK(int lineNumber, Order order){
        this.lineNumber = lineNumber;
        this.order = order;
    }

    @Override
    public boolean equals(Object olp) {

        try{
            olp = (OrderLinePK) olp;
        }catch (Exception e){
            return false;
        }

        if (lineNumber == ((OrderLinePK) olp).getLineNumber() && order.getId1c().equals(((OrderLinePK) olp).getOrder().getId1c())){
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        String str = String.valueOf(lineNumber) + "000" + String.valueOf(order.getId());
        return Integer.valueOf(str);
    }
}
