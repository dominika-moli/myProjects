package sk.tuke.kpi.oop.game.actions;

import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;


public class Move<A extends Movable> implements Action<A> {

    private A actor;
    private Direction direction;
    private float duration;
    private int firstTime;
    private boolean isDone;


    public Move(Direction direction, float duration) {
        this.direction = direction;
        this.duration = duration;
        isDone = false;
        firstTime = 0;
    }

    private Move(Direction direction) {
        this.direction = direction;
        isDone = false;
        firstTime = 0;
    }


    @Nullable
    @Override
    public A getActor() {
        return actor;
    }

    @Override
    public void setActor(@Nullable A movable) {
        this.actor = movable;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void reset() {
        actor.stoppedMoving();
        isDone = false;
        firstTime = 0;
        duration = 0;
    }

    @Override
    public void execute(float deltaTime) {
        if (getActor() == null) {
            return;
        }
        duration = duration - deltaTime;

        if (!isDone()) {
            if (firstTime == 0 ) {
                actor.startedMoving(direction);
                firstTime = firstTime + 1;
            }
            if (duration > 0) {
                actor.setPosition(actor.getPosX() + direction.getDx() * actor.getSpeed(), actor.getPosY() + direction.getDy() * actor.getSpeed());
                if ((getActor().getScene()).getMap().intersectsWithWall(actor)) {
                    actor.setPosition(actor.getPosX() - direction.getDx() * actor.getSpeed(), actor.getPosY() - direction.getDy() * actor.getSpeed());
                    actor.collidedWithWall();
                }
            } else
                stop();
        }
    }

    public void stop() {
        if (actor != null) {
            isDone = true;
            actor.stoppedMoving();
        }
    }
}


