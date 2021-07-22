package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements Switchable, EnergyConsumer {
    private Animation onAnimation;
    private Animation offAnimation;
    private boolean isOn;
    private boolean isPowered;


    public Light() {
        onAnimation = new Animation("sprites/light_on.png");
        offAnimation = new Animation("sprites/light_off.png");
        setAnimation(offAnimation);
        isOn = false;
        isPowered = false;
    }

    private void updateAnimation() {
        if (this.isOn) {
            if (this.isPowered) {
                setAnimation(onAnimation);
            } else {
                setAnimation(offAnimation);
            }
        }
    }

    public void toggle() {
            isOn = !isOn;
        updateAnimation();
    }

    public void turnOn() {
        this.isOn = true;
        updateAnimation();
    }


    public void turnOff() {
        this.isOn= false;
        updateAnimation();
    }

    public boolean isOn() {
        return isOn;
    }

    public void setPowered(boolean powered) {
        this.isPowered = powered;
        updateAnimation();

    }
}
