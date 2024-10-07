package main.model.goods.goodpropery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "good_property_values")
@NoArgsConstructor
public class GoodPropertyValue {
    // region FIELDS
    @EmbeddedId
    private GoodPropertyPK goodPropertyPK;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "digit_value")
    private BigDecimal digitValue;

    @Column(name = "bool_value")
    private Boolean boolValue;
    //endregion

    //region CONSTRUCTORS
    /**
     * Перегруженные конструкторы класса
     * @param pk
     * @param value
     */
    public <T> GoodPropertyValue(GoodPropertyPK pk, T value) {
        this.goodPropertyPK = pk;
        setValue(value);
    }

    public GoodPropertyValue(GoodPropertyPK pk, BigDecimal value) {
        this.goodPropertyPK = pk;
        setValue(null, value, null);
    }

    public GoodPropertyValue(GoodPropertyPK pk, Boolean value) {
        this.goodPropertyPK = pk;
        setValue(null, null, value);
    }
    //endregion

    //region METHODS
    private void setValue(String v1, BigDecimal v2, Boolean v3){
        stringValue = v1;
        digitValue = v2;
        boolValue = v3;
    }

    private <T> void setValue(T value){
        if (value instanceof Boolean){
            boolValue = (Boolean) value;
            return;
        }

        if (value instanceof BigDecimal){
            digitValue = (BigDecimal) value;
            return;
        }

        if (value instanceof  String){
            stringValue = (String) value;
        }
    }

    public <T> T getValue(){
        if (stringValue != null) return (T) stringValue;
        if (digitValue != null) return (T) digitValue;
        return (T) boolValue;
    }
    //endregion
}
