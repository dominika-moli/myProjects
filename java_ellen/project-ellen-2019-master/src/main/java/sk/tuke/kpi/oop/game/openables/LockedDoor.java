package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;

public class LockedDoor extends Door {
    private boolean locked;

    public LockedDoor() {
        super();
        locked= true;
    }
    public void lock() {
        locked = true;
        this.close();
    }
    public void unlock() {
        locked = false;
        this.open();
    }

    @Override
    public void useWith(Actor actor) {
        if (!isLocked())
            super.useWith(actor);
    }

    private boolean isLocked() {
        return locked;
    }


}
