package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MovableController implements KeyboardListener {

    private Movable actor;
     private Set<Input.Key> klavesy;
    private Input.Key key1 =null;
    private Input.Key key2 =null;
    private Disposable disposable;


    public MovableController(Movable actor) {
        this.actor = actor;
        klavesy= new HashSet<>();
    }

    private Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(
        Map.entry(Input.Key.UP, Direction.NORTH),
        Map.entry(Input.Key.RIGHT, Direction.EAST),
        Map.entry(Input.Key.DOWN, Direction.SOUTH),
        Map.entry(Input.Key.LEFT, Direction.WEST)

    );

    private Move<Movable> move = null;


    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (keyDirectionMap.containsKey(key)) {
            klavesy.add(key);

            if(key1==null)
            {
                key1 = key;
            }
            else if(key2==null)
            {
                key2=key;
            }

            updateMove();
        }

    }

    private void updateMove() {
        Direction pomSmer = null;
        int i = 0;
        for (Input.Key kluc:klavesy) {
            if (i==0)
                pomSmer=keyDirectionMap.get(kluc);
            if (i==1)
                pomSmer = pomSmer.combine(keyDirectionMap.get(kluc));
            i++;
        }
        stopMoving();

        if (pomSmer!=null) {
            move = new Move<>(pomSmer, Float.MAX_VALUE);
           disposable = move.scheduleFor(actor);
        }
    }

    private void stopMoving() {
        if (move!=null) {
            move.stop();
            disposable.dispose();
            move=null;
        }

    }

    @Override
    public void keyReleased(@NotNull Input.Key key) {
        if(keyDirectionMap.containsKey(key))
        { klavesy.remove(key);

            if (key== key1) {
                key1 =null;
            }

            if (key== key2) {
                key2 =null;
            }

            updateMove();
        }
    }

}
