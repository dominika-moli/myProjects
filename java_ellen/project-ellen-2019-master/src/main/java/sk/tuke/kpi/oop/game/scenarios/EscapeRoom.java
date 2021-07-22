package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.oop.game.ArmedEgg;
import sk.tuke.kpi.oop.game.LargeBox;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.behaviours.FollowingRipley;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.AlienMother;
import sk.tuke.kpi.oop.game.characters.ArmedAlien;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.iterator.MojIterator;
import sk.tuke.kpi.oop.game.iterator.ZoznamPredmetov;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

//import sk.tuke.kpi.oop.game.Locker;

public class EscapeRoom implements SceneListener {


    public static class Factory implements ActorFactory {
        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            assert name != null;
            switch (name)
            {
                case "ellen":
                    return new Ripley();
                case "energy":
                    return new Energy();
              //  case "access card":
                //    return new AccessCard();
                case "door":
                    return new LockedDoor();
                //case "locker":
                  //  return new Locker();
                //case "ventilator":
                  //  return new Ventilator();
                case "alien":
                    return new Alien(100, new RandomlyMoving());
                case "alien mother":
                    return new AlienMother(200, new RandomlyMoving());
                case "ammo":
                    return new Ammo();
                //case "alien mother":
                //    return new AlienMother();
                default: return null;
            }
        }
    }


    @Override
    public void sceneCreated(@NotNull Scene scene) {

    }

    @Override
    public void sceneInitialized(@NotNull Scene scene) {

        ZoznamPredmetov zoznamPredmetov = new ZoznamPredmetov();
        for(MojIterator iterator = zoznamPredmetov.getIterator(); iterator.hasNext();) {
            String predmet = (String) iterator.next();
            System.out.println("Predmet : " + predmet);
        }


        Ripley ellen= scene.getFirstActorByType(Ripley.class);
        scene.follow(ellen);

        Disposable movableCon = scene.getInput().registerListener(new MovableController(ellen));
        Disposable keeperCon = scene.getInput().registerListener(new KeeperController(ellen));
        Disposable shooterCon = scene.getInput().registerListener(new ShooterController(ellen));

        //FireExtinguisher fireExtinguisher= new FireExtinguisher();
    //  ellen.getBackpack().add(fireExtinguisher);
     //   scene.getGame().pushActorContainer(ellen.getBackpack());


        LargeBox largeBox= new LargeBox();
        scene.addActor(largeBox, 200, 400);

        //Teleport teleport = new Teleport();
      //  scene.addActor(teleport, 300, 100);

        LockedDoor lockedDoor= new LockedDoor();
        scene.addActor(lockedDoor, 300, 100);


        Alien alien  = new Alien(100, new FollowingRipley());
        scene.addActor(alien, 300, 100);

       // Barrel barrel  = new Barrel ();
      //  scene.addActor(barrel, 300, 100);

       // AlienMother alienMother = new AlienMother(100, new FollowingRipley());
      //  scene.addActor(alienMother, 300, 100);


        ArmedAlien armedAlien = new ArmedAlien(100,new RandomlyMoving());
        scene.addActor(armedAlien, 280, 400);

        AccessCard accessCard = new AccessCard();
        ellen.getBackpack().add(accessCard);

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, (Ripley) -> ellen.decreaseEnergy());


        ArmedEgg armedEgg = new ArmedEgg();
        scene.addActor(armedEgg, 250, 100);

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, (ArmedEgg) -> armedEgg.zapniVajko());


        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, (Ripley) -> movableCon.dispose());
        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, (Ripley) -> keeperCon.dispose());
        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, (Ripley) -> shooterCon.dispose());

        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, (Ripley) -> ellen.stopDecreasingEnergy().dispose());
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley ellen= scene.getFirstActorByType(Ripley.class);
        assert ellen != null;
        ellen.showRipleyState();
    }
}
