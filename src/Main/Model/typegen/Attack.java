package Main.Model.typegen;


import Main.Model.role.NullTextSegment;
import Main.Relation;

/**
 * Created by LuckyP on 16.12.17.
 */
public abstract class Attack extends Relation{

    public Attack() {
        sourceSegment = new NullTextSegment();
        target = new NullTextSegment();
    }

    public Attack(String relationId, String src, String trg) {
        super(relationId, src, trg);
    }
}
