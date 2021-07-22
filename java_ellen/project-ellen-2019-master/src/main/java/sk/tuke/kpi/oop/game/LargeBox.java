package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.prototype.CloneableObject;
import sk.tuke.kpi.oop.game.items.Hamburger;
import sk.tuke.kpi.oop.game.weapons.Bullet;

import java.util.List;
import java.util.Objects;

public class LargeBox extends CloneableObject {

    public LargeBox() {
        setAnimation(new Animation("sprites/box_large.png", 16, 16));
    }

    public void zasiaholBox() {
        List<Actor> bulletsList;
        bulletsList = Objects.requireNonNull(getScene()).getActors();
        int pomX = this.getPosX();
        int pomY = this.getPosY();

        for (Actor gulka : bulletsList) {
            if (gulka instanceof Bullet && gulka.intersects(this)) {
                getScene().addActor(new Hamburger(), pomX, pomY);
                getScene().removeActor(this);
            }
        }
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
            new Loop<>(
            new Invoke<>(this::zasiaholBox)
        ).scheduleOn(scene);
    }
}
