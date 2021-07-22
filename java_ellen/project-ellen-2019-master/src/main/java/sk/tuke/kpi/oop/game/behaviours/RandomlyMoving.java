package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

public class RandomlyMoving implements Behaviour<Movable> {

    public void randomMove(Movable actor) {
        int pomX = (int) (Math.random() * (3)) - 1;
        int pomY = (int) (Math.random() * (3)) - 1;

        Direction smer = null;

        for (Direction value : Direction.values()) {
            if (pomX == value.getDx() && pomY == value.getDy()) {
                smer = value;
            }
        }
        assert smer != null;
        actor.getAnimation().setRotation(smer.getAngle());
        new Move<>(smer, 2).scheduleFor(actor);
    }

    @Override
    public void setUp(Movable movable) {

        if (movable==null) {
            return;
        } else {
            new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(this::randomMove),
                    new Wait<>(2)
                )).scheduleFor(movable);
        }

    }

}

