package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.prototype.CloneableObject;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Hammer;

import java.util.List;
import java.util.Objects;

public class Barrel  extends CloneableObject {


    public Barrel() {
        setAnimation(new Animation("sprites/barrel.png",16,16));
        }

    public void rozbiSa() {
        List<Actor> nastrojeList;
        nastrojeList = Objects.requireNonNull(getScene()).getActors();
        int pomX = this.getPosX();
        int pomY = this.getPosY();

        for (Actor nastroj : nastrojeList) {
            if (nastroj instanceof Hammer && nastroj.intersects(this)) {
                getScene().addActor(new AccessCard(), pomX, pomY);
                getScene().removeActor(this);
                getScene().removeActor(nastroj);
            }
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(
            new Invoke<>(this::rozbiSa)
        ).scheduleOn(scene);
    }
}

