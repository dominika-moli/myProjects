package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;
import java.util.List;
import java.util.Objects;


public class Take<K extends Keeper> extends AbstractAction<K> {

    public Take() {

    }


    @Override
    public void execute(float deltaTime) {

         if (getActor() == null || getActor().getScene() == null) {
           setDone(true);
          return;
          }
        Scene scene=getActor().getScene();

        if (!isDone()) {
            List<Actor> nastrojeList;
            nastrojeList = Objects.requireNonNull(getActor().getScene()).getActors();

            for (Actor nastroj : nastrojeList) {
                if (nastroj instanceof Collectible && nastroj.intersects(getActor())) {
                    try {
                        getActor().getBackpack().add(((Collectible) nastroj));

                        assert scene != null;
                        scene.removeActor(nastroj);
                        break;

                    } catch (IllegalStateException exception) {
                        scene.getOverlay().drawText(exception.getMessage(), 0, 0).showFor(2);
                    }
                  }
            }
            setDone(true);

        }
    }
}






