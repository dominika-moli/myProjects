package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

import java.util.List;

public class Alien extends AbstractActor implements Movable, Alive, Enemy {
    private Health health;
    private Behaviour<? super Alien> spravanie;
    private Disposable attack = null;

    public Alien() {
        setAnimation(new Animation("sprites/alien.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG));
        health = new Health(100, 100);
        health.onExhaustion(() -> getScene().removeActor(this));
    }

    public Alien(int healthValue, Behaviour<? super Alien> behaviour) {
        setAnimation(new Animation("sprites/alien.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG));
        health = new Health(healthValue, 100);
        spravanie = behaviour;
        health.onExhaustion(() -> getScene().removeActor(this));
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
          //  return;
       // } else {
            spravanie.setUp(this);
        }
        attack = new Loop<>(
            new ActionSequence<>(
            new Invoke<>(this::vycucajAlive),
            new Wait<>(0.3f)
        )).scheduleFor(this);
    }

    public void prestanCucat() {
        if (attack == null) {
            return;
        } else {
            attack = null;
            //attack.dispose();
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
                ((Alive) aliveActor).getHealth().drain(3);
                    new ActionSequence<>(
                        new Invoke<>(this::prestanCucat),
                        new Wait<>(1),
                        new Invoke<>(this::vycucajAlive)
                    ).scheduleFor(this);


                }

            }
        }
    }

