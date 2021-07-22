package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.oop.game.scenarios.MojScenar;

//import sk.tuke.kpi.oop.game.scenarios.FirstSteps;


public class Main {
    public static void main(String[] args) {

        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);
        Game game = new GameApplication(windowSetup);
        Scene scene = new World("World", "maps/final_map.tmx", new MojScenar.Factory());
       // FirstSteps firstSteps = new FirstSteps();
      //  MissionImpossible missionImpossible = new MissionImpossible();


        //EscapeRoom escapeRoom = new EscapeRoom();
        //TestPrototype testPrototype = new TestPrototype();
        MojScenar mojScenar = new MojScenar();

        game.addScene(scene);
        scene.addListener(mojScenar);
        game.start();
        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);
    }
}
