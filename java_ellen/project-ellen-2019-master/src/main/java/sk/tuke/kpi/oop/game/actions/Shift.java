package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;


public class Shift <A extends Keeper> extends AbstractAction<A> {

    @Override
    public void execute(float deltaTime) {
        if (getActor()==null || isDone()) {
        setDone(true);
        return;
    }
        if (getActor().getBackpack().peek()==null) {
            setDone(true);
            return;
        }
        if (!isDone()) {
            getActor().getBackpack().shift();
            setDone(true);
        }
    }
}
