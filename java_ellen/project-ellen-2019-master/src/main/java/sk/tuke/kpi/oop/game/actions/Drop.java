package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;


public class Drop <A extends Keeper> extends AbstractAction<A> {



    @Override
    public void execute(float deltaTime) {
        //Scene scene = (getActor()).getScene();

        if (getActor()==null) {
            setDone(true);
            return;
        }

        if (getActor().getBackpack().peek()==null || getActor().getScene()==null) {
            setDone(true);
            return;
        }

    if (!isDone()) {

       Collectible poslednyNastroj = getActor().getBackpack().peek();
        assert poslednyNastroj != null;
       // assert scene != null;
        getActor().getScene().addActor(poslednyNastroj, (getActor().getPosX() + (getActor().getWidth()-poslednyNastroj.getWidth()/2)), (getActor().getPosY() + (getActor().getHeight()-poslednyNastroj.getHeight()/2)));
        getActor().getBackpack().remove(poslednyNastroj);
       // setDone(true);
     }
        setDone(true);
    }
}
