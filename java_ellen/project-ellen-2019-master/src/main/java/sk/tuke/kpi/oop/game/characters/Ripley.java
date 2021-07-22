package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gun;

import java.util.Objects;

public class Ripley extends AbstractActor implements Movable, Keeper, Alive, Armed{
    private Animation ripleyAnimation;
    private int speed;
    //private int energy;
    private int ammo;
    private Backpack backpack;
    private Disposable disposable=null;
    private Health health;
    private Firearm gun;

    public static final Topic<Ripley> RIPLEY_DIED = Topic.create("ripley died", Ripley.class);

    public Ripley() {
        super("Ellen");
        ripleyAnimation = new Animation("sprites/player.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(ripleyAnimation);
        speed = 3;
        ripleyAnimation.stop();
        //energy = 100;
        ammo = 100;
        backpack = new Backpack("Ripley's backpack",10);
        health = new Health(100, 100);
        gun = new Gun (100,150);

        health.onExhaustion(() -> {
            this.setAnimation(new Animation("sprites/player_die.png",32,32,0.1f, Animation.PlayMode.ONCE));
            Objects.requireNonNull(getScene()).getMessageBus().publish(RIPLEY_DIED,this);
        });
    }

    @Override
    public int getSpeed() {
        return this.speed;
    }

    @Override
    public void startedMoving(Direction direction) {
        ripleyAnimation.setRotation(direction.getAngle());
        ripleyAnimation.play();
    }

    @Override
    public void stoppedMoving() {
        ripleyAnimation.stop();
    }


    /* public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
    this.energy = energy;
    }
     */

    public int getAmmo() { return ammo; }

    public void setAmmo(int ammo) { this.ammo = ammo; }

    @Override
    public Backpack getBackpack() {
        return backpack;
    }

    public void showRipleyState() {
        int windowHeight = Objects.requireNonNull(getScene()).getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        getScene().getGame().getOverlay().drawText("Energy " +health.getValue(), 120, yTextPos);
        getScene().getGame().getOverlay().drawText("Your Ammo " + this.getFirearm().getAmmo(), 280, yTextPos);
        getScene().getGame().getOverlay().drawText("Maximum Ammo " + this.getFirearm().getMaxAmmo(), 450, yTextPos);
    }


    public void decreaseEnergy() {

        if (this.health.getValue() <= 0) {
            this.setAnimation(new Animation("sprites/player_die.png", 32, 32, 0.1f, Animation.PlayMode.ONCE));
            Objects.requireNonNull(getScene()).getMessageBus().publish(RIPLEY_DIED, this);
        }
        else {
            disposable = new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(() -> { if (this.health.getValue() <= 0) {
                        this.setAnimation(new Animation("sprites/player_die.png", 32, 32, 0.1f, Animation.PlayMode.ONCE));
                        Objects.requireNonNull(getScene()).getMessageBus().publish(RIPLEY_DIED, this);
                        return;}
                    else {this.getHealth().drain(5);}
                    }),
                    new Wait<>(1)
                )
            ).scheduleFor(this);
        }
    }

    public Disposable stopDecreasingEnergy() {
        return disposable;
    }

    @Override
    public Health getHealth() {
        return health;
    }

   @Override
   public Firearm getFirearm() {
      return gun;
    }

    @Override
    public void setFirearm(Firearm weapon) {
        gun = weapon;
    }

    public void setSpeed(int i) {
        this.speed = i;
    }
}

