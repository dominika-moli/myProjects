package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.items.Usable;

import java.util.Objects;


public class Door extends AbstractActor implements Openable, Usable<Actor> {
    private boolean opened;
    public static final Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static final Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);
    private enum  Orientation { VERTICAL, HORIZONTAL }
    private Animation openedDoorAnimation;
    private Animation closedDoorAnimation;
    private String sourceOfAnimation = "sprites/vdoor.png";


    public Door() {
        closedDoorAnimation = new Animation(sourceOfAnimation, 16, 32, 0.1f, Animation.PlayMode.ONCE);
        openedDoorAnimation = new Animation(sourceOfAnimation, 16, 32, 0.1f, Animation.PlayMode.ONCE_REVERSED);
       // Animation doorAnimation = new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(new Animation(sourceOfAnimation, 16, 32));
        getAnimation().stop();
        opened = false;
    }

    public Door (String name, Orientation orientation) {
        super(name);
        opened = false;
        if (orientation == Orientation.HORIZONTAL) {
            sourceOfAnimation = "sprites/hdoor.png";
            closedDoorAnimation = new Animation(sourceOfAnimation, 16, 32, 0.1f, Animation.PlayMode.ONCE);
            openedDoorAnimation = new Animation(sourceOfAnimation, 16, 32, 0.1f, Animation.PlayMode.ONCE_REVERSED);
            setAnimation(new Animation(sourceOfAnimation, 16, 32));
           // setAnimation(new Animation("sprites/hdoor.png", 32, 16, 0.1f, Animation.PlayMode.ONCE));
            getAnimation().stop();
        }
        else if (orientation == Orientation.VERTICAL) {
            sourceOfAnimation = "sprites/vdoor.png";
            closedDoorAnimation = new Animation(sourceOfAnimation, 16, 32, 0.1f, Animation.PlayMode.ONCE);
            openedDoorAnimation = new Animation(sourceOfAnimation, 16, 32, 0.1f, Animation.PlayMode.ONCE_REVERSED);
            setAnimation(new Animation(sourceOfAnimation, 16, 32));
            //setAnimation(new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.ONCE));
            getAnimation().stop();
        }

    }

    @Override
    public void useWith(Actor actor) {
        if (isOpen())
            close();
        else
            open();
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    @Override
    public void open() {
        opened = true;
        Objects.requireNonNull(getScene()).getMap().getTile(getPosX() / 16, getPosY() / 16).setType(MapTile.Type.CLEAR);
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16 + 1).setType(MapTile.Type.CLEAR);
        setAnimation(openedDoorAnimation);
        //getAnimation().setPlayMode(Animation.PlayMode.ONCE_REVERSED);
        getAnimation().play();
        getAnimation().stop();
        getScene().getMessageBus().publish(DOOR_OPENED, this);

    }

    @Override
    public void close() {
        opened = false;
        Objects.requireNonNull(getScene()).getMap().getTile(getPosX() / 16, getPosY() / 16).setType(MapTile.Type.WALL);
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16 + 1).setType(MapTile.Type.WALL);
        setAnimation(closedDoorAnimation);
        //getAnimation().setPlayMode(Animation.PlayMode.ONCE);
        getAnimation().play();
        getAnimation().stop();
        getScene().getMessageBus().publish(DOOR_CLOSED, this);
    }

    @Override
    public boolean isOpen() {
        return opened;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        Objects.requireNonNull(getScene()).getMap().getTile(this.getPosX() /16, this.getPosY() / 16).setType(MapTile.Type.WALL);
        getScene().getMap().getTile(this.getPosX() /16, this.getPosY() / 16+1 ).setType(MapTile.Type.WALL);
    }
}
