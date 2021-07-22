package sk.tuke.kpi.oop.game.prototype;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.framework.AbstractActor;

public abstract class CloneableObject extends AbstractActor implements Cloneable {

    public CloneableObject() {
        super();
    }

    public CloneableObject(String name) {
        super(name);
    }
       @NotNull
    public String getName(){
        return super.getName();
    }

    public Object clone() {
        Object clone = null;

        try {
            clone = super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

}
