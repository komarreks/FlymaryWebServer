package main.model.goods.characs.characproperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.goods.characs.Charac;
import main.model.goods.goodpropery.GoodPropertyPK;
import main.model.propertyes.Property;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "charac_property_values")
@NoArgsConstructor
public class CharacPropertyValue {

    // region FIELDS
    @EmbeddedId
    private CharacPropertyPK characPropertyPK;

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
    public <T> CharacPropertyValue(CharacPropertyPK pk, T value) {
        this.characPropertyPK = pk;
        setValue(value);
    }

    public CharacPropertyValue(CharacPropertyPK pk, BigDecimal value) {
        this.characPropertyPK = pk;
        setValue(null, value, null);
    }

    public CharacPropertyValue(CharacPropertyPK pk, Boolean value) {
        this.characPropertyPK = pk;
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
