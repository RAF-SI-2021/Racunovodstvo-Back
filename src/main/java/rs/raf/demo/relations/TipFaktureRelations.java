package rs.raf.demo.relations;

import rs.raf.demo.exceptions.OperationNotSupportedException;
import rs.raf.demo.model.enums.TipFakture;

import javax.persistence.criteria.*;

public class TipFaktureRelations<T> extends RacunRelations<T>{

    private final Expression<TipFakture> tipFaktureExpression;
    private final TipFakture tipFaktureValue;

    public TipFaktureRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        tipFaktureExpression = root.get("tipFakture").as(TipFakture.class);
        tipFaktureValue = TipFakture.valueOf(val);
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
        return this.builder.equal(tipFaktureExpression,tipFaktureValue);
    }
}
