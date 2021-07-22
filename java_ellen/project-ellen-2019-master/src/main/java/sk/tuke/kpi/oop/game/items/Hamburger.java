package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;


public class Hamburger extends AbstractActor implements Usable <Ripley> {

    public Hamburger() {
        setAnimation(new Animation("sprites/hamburger.png",16,16));
    }

    @Override
    public void useWith(Ripley actor) {
        if (actor == null) return;
        actor.getHealth().restore();
        if (actor.getHealth().getValue() == 100) {
            getScene().removeActor(this);
        }
            new ActionSequence<Ripley>(
            new Invoke<>(() -> actor.setSpeed(5)),
            new Wait<>(5),
            new Invoke<>(() -> actor.setSpeed(2))
        ).scheduleFor(actor);
        }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
