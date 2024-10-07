package main.model.goods.characs.characproperty;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.goods.characs.Charac;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CharacPropertyPK {

    @ManyToOne
    @JoinColumn(name = "charac_id")
    private Charac charac;

    private String property;

    public CharacPropertyPK(Charac charac, String property){
        this.charac = charac;
        this.property = property;
    }



}
