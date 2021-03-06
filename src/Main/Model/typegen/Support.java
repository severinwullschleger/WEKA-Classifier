package Main.Model.typegen;


import Main.Model.role.NullTextSegment;
import Main.Relation;
import Main.TextSegment;

/**
 * Created by LuckyP on 16.12.17.
 */
public abstract class Support extends Relation {

    public Support() {
        sourceSegment = new NullTextSegment();
        target = new NullTextSegment();
    }

    public Support(String relationId, String src, String trg) {
        super(relationId, src, trg);
    }

    public Support(TextSegment textSegment) {
        super(textSegment);
    }

    public final String getWekaAttackOrSupport() {
        return "sup";
    }

    @Override
    public boolean isAttack() {
        return false;
    }

    @Override
    public boolean isValidRelation() {
        return true;
    }

    @Override
    public void setTargetId(int id) {
        targetId = "a"+id;
    }
}
