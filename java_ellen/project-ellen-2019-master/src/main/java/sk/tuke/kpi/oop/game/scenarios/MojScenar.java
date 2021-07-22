package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.oop.game.ArmedEgg;
import sk.tuke.kpi.oop.game.Barrel;
import sk.tuke.kpi.oop.game.LargeBox;
import sk.tuke.kpi.oop.game.Teleport;
import sk.tuke.kpi.oop.game.behaviours.FollowingRipley;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.AlienMother;
import sk.tuke.kpi.oop.game.characters.ArmedAlien;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.items.FireExtinguisher;
import sk.tuke.kpi.oop.game.iterator.MojIterator;
import sk.tuke.kpi.oop.game.iterator.ZoznamPredmetov;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;

public class MojScenar implements SceneListener {

    public static class Factory implements ActorFactory {
        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            assert name != null;
            assert type != null;
         //   if (name.equals("alien") && type.equals("following")) {
           //     return new Alien(100, new FollowingRipley());
            //}
            switch (name)
            {
                case "ellen":
                    return new Ripley();
                case "energy":
                    return new Energy();
                 case "door":
                    return new LockedDoor();
                case "alien":
                    return new Alien(100, new RandomlyMoving());
                case "alien mother":
                    return new AlienMother(200, new RandomlyMoving());
                case "ammo":
                    return new Ammo();
                case "armed alien":
                    return new ArmedAlien(100, new RandomlyMoving());
                case "large box":
                    return new LargeBox();
                case "teleport":
                    return new Teleport();
                case "barrel":
                    return new Barrel();
                case "armed egg":
                    return new ArmedEgg();

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

       Ripley ellen = scene.getFirstActorByType(Ripley.class);
       scene.follow(ellen);

        FireExtinguisher fireExtinguisher= new FireExtinguisher();
        ellen.getBackpack().add(fireExtinguisher);
        scene.getGame().pushActorContainer(ellen.getBackpack());

        ArmedEgg armedEgg = scene.getFirstActorByType(ArmedEgg.class);

        Disposable movableCon = scene.getInput().registerListener(new MovableController(ellen));
        Disposable keeperCon = scene.getInput().registerListener(new KeeperController(ellen));
        Disposable shooterCon = scene.getInput().registerListener(new ShooterController(ellen));

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, (Ripley) -> ellen.decreaseEnergy());
        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, (Ripley) -> movableCon.dispose());
        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, (Ripley) -> keeperCon.dispose());
        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, (Ripley) -> shooterCon.dispose());
        scene.getMessageBus().subscribe(Door.DOOR_OPENED, (ArmedEgg) -> {
            assert armedEgg != null;
            armedEgg.zapniVajko();
        });

        Alien alien  = new Alien(100, new FollowingRipley());
        scene.addActor(alien, 30, 100);

        Alien alien2  = new Alien(100, new FollowingRipley());
        scene.addActor(alien2, 500, 100);

        Alien alien3  = new Alien(100, new FollowingRipley());
        scene.addActor(alien3, 700, 100);

        ArmedAlien armedAlien  = new ArmedAlien(100, new RandomlyMoving());
        scene.addActor(armedAlien, 400, 100);

    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley ellen= scene.getFirstActorByType(Ripley.class);
        assert ellen != null;
        ellen.showRipleyState();
    }

}
