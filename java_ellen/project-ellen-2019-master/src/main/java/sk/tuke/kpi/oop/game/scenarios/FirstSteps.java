package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.*;

public class FirstSteps implements SceneListener {
    private Ripley ripley;
    private Energy energy;
    private Ammo ammo;
   // Hammer hammer = new Hammer();
 //   Hammer hammer1 = new Hammer();
   // Hammer hammer2= new Hammer();
  //  Hammer hammer3= new Hammer();
   // Hammer hammer4= new Hammer();
  //  Hammer hammer5 = new Hammer();
   // FireExtinguisher fireExtinguisher = new FireExtinguisher();
  //  FireExtinguisher fireExtinguisher1 = new FireExtinguisher();
  //  FireExtinguisher fireExtinguisher2 = new FireExtinguisher();
  //  FireExtinguisher fireExtinguisher3 = new FireExtinguisher();
  //  FireExtinguisher fireExtinguisher4 = new FireExtinguisher();
  //  FireExtinguisher fireExtinguisher5 = new FireExtinguisher();
   // Wrench wrench = new Wrench();


    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        ripley = new Ripley();
        scene.addActor(ripley, 0, 0);
       // ripley.setEnergy(30);
        // ripley.setAmmo(500);

        MovableController movableController = new MovableController(ripley);
        scene.getInput().registerListener(movableController);



        energy = new Energy();
        scene.addActor(energy,-100, 50);

        new When<>(
            () -> ripley.intersects(energy),
            new Invoke<>(() -> energy.useWith(ripley))
        ).scheduleFor(ripley);



        ammo = new Ammo();
        scene.addActor(ammo,-200, 50);

        new When<>(
            () -> ripley.intersects(ammo),
            new Invoke<>(() -> ammo.useWith(ripley))
        ).scheduleFor(ripley);


      //  scene.addActor(hammer, 100, -50);
       // scene.addActor(hammer1, 200, -50);
       // scene.addActor(hammer2, 250, -50);
        //scene.addActor(hammer3, 150, -50);
       // scene.addActor(hammer4, 50, -50);
      //  scene.addActor(hammer5, 50, 50);
      //  scene.addActor(fireExtinguisher, 120, 40);
       // scene.addActor(fireExtinguisher1, 130, 40);
       // scene.addActor(fireExtinguisher2, 140, 40);
       // scene.addActor(fireExtinguisher3, 160, 40);
       // scene.addActor(fireExtinguisher4, 100, 40);
     //   scene.addActor(wrench, -150, 200);

       // ripley.getBackpack().add(hammer);
       // ripley.getBackpack().add(fireExtinguisher);
        //ripley.getBackpack().add(wrench);

        //scene.getGame().pushActorContainer(ripley.getBackpack());
        //ripley.getBackpack().shift();

        KeeperController keeperController = new KeeperController(ripley);
        scene.getInput().registerListener(keeperController);

    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        scene.getGame().getOverlay().drawText("Energy: " +ripley.getHealth().getValue(), 120, yTextPos);
        scene.getGame().getOverlay().drawText("Ammo: " +ripley.getAmmo(), 320, yTextPos);

        //scene.getGame().getOverlay().drawText("cau",120,20);
      }

}

