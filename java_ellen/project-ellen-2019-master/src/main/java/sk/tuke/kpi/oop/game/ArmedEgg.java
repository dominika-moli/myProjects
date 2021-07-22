package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.Fire;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.LaserGun;

import java.util.Objects;

public class ArmedEgg extends AbstractActor implements Armed {
    private Firearm zbran;

    public ArmedEgg() {
        setAnimation(new Animation("sprites/alien_egg.png", 32, 32));
        zbran = new LaserGun(100, 100);
        getAnimation().setRotation(270);
    }

    @Override
    public Firearm getFirearm() {
        return zbran;
    }

    @Override
    public void setFirearm(Firearm weapon) {
        zbran = weapon;
    }

    public void strielajVajko() {
        new Loop<>(
            new ActionSequence<>(
                new Wait<>(3),
                new Invoke<>(
                    () -> new Fire<>().scheduleFor(this)),
                new Wait<>(5)
            )
        ).scheduleOn(Objects.requireNonNull(getScene()));
    }


    public void zapniVajko(){

        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(this::strielajVajko),
                new Wait<>(1)
            )).scheduleFor(this);
    }
}



