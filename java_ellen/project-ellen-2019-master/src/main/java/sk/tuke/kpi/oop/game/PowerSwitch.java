package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Color;

public class PowerSwitch extends AbstractActor {
    private Switchable switchable;

    public PowerSwitch(Switchable switchable) {
        Animation switchAnimation = new Animation("sprites/switch.png", 16, 16);
        setAnimation(switchAnimation);
        this.switchable = switchable;
    }

    public Switchable getDevice(){
        return this.switchable;
    }

    public void switchOn() {
        if (switchable == null) {
            return;
        }
        else {
            this.switchable.turnOn();
            getAnimation().setTint(Color.WHITE);
        }
    }

    public void switchOff() {
        if (switchable == null) {
            return;
        }
        else {
            this.switchable.turnOff();
            getAnimation().setTint(Color.GRAY);
        }
    }
    }


