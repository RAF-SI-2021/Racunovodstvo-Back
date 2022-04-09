package rs.raf.demo.relations;

import rs.raf.demo.exceptions.OperationNotSupportedException;
import rs.raf.demo.model.enums.RadnaPozicija;
import rs.raf.demo.model.enums.TipFakture;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RadnaPozicijaRelations extends RacunRelations{

    private final Expression<RadnaPozicija> radnaPozicijaExpression;
    private final RadnaPozicija radnaPozicija;

    public RadnaPozicijaRelations(Root root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        radnaPozicijaExpression = root.get("radnaPozicija").as(RadnaPozicija.class);
        radnaPozicija = RadnaPozicija.valueOfLabel(val);
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija > nije implementirana za TipFakture [key:%s,val%s]", key, val));
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        throw new OperationNotSupportedException(String.format("Relacija < nije implementirana za TipFakture [key:%s,val%s]",key,val));
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(radnaPozicijaExpression, radnaPozicija);
    }
}
