package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.List;
import java.util.Objects;

public class Teleport extends AbstractActor {

    public Teleport(){
        setAnimation(new Animation("sprites/lift.png"));
    }

    public void vstupilHrac () {
        List<Actor> actorsList;
        actorsList = Objects.requireNonNull(getScene()).getActors();

            for (Actor postava : actorsList) {
                if (postava instanceof Ripley && postava.intersects(this)) {
                    {// getScene().removeActor(postava);
                        postava.getAnimation().stop();
                        koniecHry();
                    }
                }
            }
        }

    private void koniecHry(){
        //System.out.println("cecerfef");
        int pomX = Objects.requireNonNull(getScene()).getGame().getWindowSetup().getWidth()/2;
        int pomY = getScene().getGame().getWindowSetup().getHeight()/2  ;
        getScene().getGame().getOverlay().drawText("YOU WON!",pomX,pomY).showFor(2.5f);
        new Wait<>(2);
        getScene().getGame().stop();
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(
            new Invoke<>(this::vstupilHrac)
        ).scheduleOn(scene);
    }

}
