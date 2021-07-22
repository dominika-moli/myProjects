package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.prototype.CloneableObject;
import sk.tuke.kpi.oop.game.actions.Fire;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gun;

import java.util.List;
import java.util.Objects;

public class ArmedAlien extends CloneableObject implements Movable, Alive, Enemy, Armed {
    private Health health;
    private Behaviour<? super ArmedAlien> spravanie;
    private Disposable cucaj = null;
    private Disposable kradni = null;
    private Firearm firearm;

    public ArmedAlien() {
        setAnimation(new Animation("sprites/lurker_alien.png", 32, 32, 0.1f));
        health = new Health(100, 100);
        health.onExhaustion(() -> getScene().removeActor(this));
        firearm = new Gun(50, 100);
    }

    public ArmedAlien(int healthValue, Behaviour<? super ArmedAlien> behaviour) {
        setAnimation(new Animation("sprites/lurker_alien.png", 32, 32, 0.1f));
        health = new Health(healthValue, 100);
        spravanie = behaviour;
        health.onExhaustion(this::odstranPridaj);
        firearm = new Gun(8000, 10000);
    }

    public void odstranPridaj () {
        getScene().addActor(new Hammer(), this.getPosX(), this.getPosY());
        getScene().removeActor(this);

    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        if (spravanie != null) {
         spravanie.setUp(this);
        }
        cucaj = new Loop<>(
            new ActionSequence<>(
                new Invoke<>(this::vycucajAlive),
                new Wait<>(0.3f)
            )).scheduleFor(this);

        kradni = new Loop<>(
            new ActionSequence<>(
                new Invoke<>(this::stealAmmo),
                new Wait<>(0.3f)
            )).scheduleFor(this);

        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(this::strielaj),
                new Wait<>(3)
            )).scheduleFor(this);
    }

    public void prestanCucat() {
        if (cucaj == null) {
            return;
        } else {
            cucaj = null;
           }
    }

    public void vycucajAlive() {
        if (getScene() == null) {
            return;
        }
        List<Actor> aliveActorsList;
        aliveActorsList = getScene().getActors();

        for (Actor aliveActor : aliveActorsList) {
            if (aliveActor instanceof Alive && !(aliveActor instanceof Enemy) && this.intersects(aliveActor)) {
                ((Alive) aliveActor).getHealth().drain(1);
                new ActionSequence<>(
                    new Invoke<>(this::prestanCucat),
                    new Wait<>(1),
                    new Invoke<>(this::vycucajAlive)
                ).scheduleFor(this);
            }

        }
    }

    public void stealAmmo() {
        if (getScene() == null) {
            return;
        }
        List<Actor> actorsList;
        actorsList = getScene().getActors();

        for (Actor actor : actorsList) {
            if (actor instanceof Ripley && this.intersects(actor)) {
               ((Ripley) actor).getFirearm().decreaseAmmo(5);
                ((Ripley) actor).setSpeed(1);
                new ActionSequence<>(
                    new Invoke<>(this::stopStealing),
                    new Wait<>(1),
                    new Invoke<>(this::stealAmmo)
                ).scheduleFor(this);
            }

        }
    }

    private void stopStealing() {
        if (kradni == null) {
            return;
        } else {
            kradni = null;
           }
    }

    @Override
    public Firearm getFirearm() {
        return firearm;
    }

    @Override
    public void setFirearm(Firearm weapon) {
        this.firearm = weapon;
    }

    private void strielaj(){
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(
                    () -> new Fire<>().scheduleFor(this)),
                new Wait<>(3)
            )
        ).scheduleOn(Objects.requireNonNull(getScene()));
    }


}
