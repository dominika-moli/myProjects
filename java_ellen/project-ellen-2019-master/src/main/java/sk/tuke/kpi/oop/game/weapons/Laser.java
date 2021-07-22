package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.graphics.Animation;

public class Laser extends Bullet {
    public Laser() {
            setAnimation(new Animation("sprites/laser_beam.png",16,16));
            this.setDamage(30);
            this.setSpeed(6);
    }

}
