package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class FollowingRipley implements Behaviour<Movable> {

    private Disposable disposable = null;
    private Move<Movable> move = null;

    public void followRipley(Movable actor) {
        int pomXX = actor.getPosX();
        int pomYY = actor.getPosY();

        int pomX = actor.getScene().getFirstActorByType(Ripley.class).getPosX();
        int pomY = actor.getScene().getFirstActorByType(Ripley.class).getPosY();


       if (pomXX != pomX) {
            if (pomXX > pomX) {
                pomXX = pomXX - 1;
            } else {
                pomXX = pomXX + 1;
            }
        }

        if (pomYY != pomY) {
            if (pomYY > pomY) {
                pomYY = pomYY - 1;
            } else {
                pomYY = pomYY + 1;
            }
        }


        pomXX = pomXX - pomX;
        pomYY = pomYY - pomY;

        if (pomXX > 0) {
            pomXX = -1;
        } else if (pomXX < 0) {
            pomXX = 1;
        }

        if (pomYY > 0) {
            pomYY = -1;
        } else if (pomYY < 0) {
            pomYY = 1;
        }

        Direction smer = null;
        for (Direction value : Direction.values()) {
            if (pomXX == value.getDx() && pomYY == value.getDy()) {
                smer = value;
            }
        }
        if (move != null) {
            move.stop();
            disposable.dispose();
            move = null;
        }
        if (smer != null) {
            move = new Move<>(smer, Float.MAX_VALUE);
            disposable = move.scheduleFor(actor);
        }
    }

        @Override
        public void setUp (Movable actor){
            if (actor != null) {
                new Loop<>(new ActionSequence<Movable>(
                    new Invoke<>(() -> {
                        followRipley(actor);
                    }),
                    new Wait<>(0.9f)
                )).scheduleFor(actor);
            }
        }
    }
