package sk.tuke.kpi.oop.game.prototype;

import sk.tuke.kpi.gamelib.SceneListener;

public class TestPrototype implements SceneListener {

    public static void main(String[] args) {
        ObjectCache.loadCache();

        CloneableObject clonedObject1 = ObjectCache.getCloneableObject("armedAlien");
        System.out.println("Object : " + clonedObject1.getName());

        CloneableObject clonedObject2 = ObjectCache.getCloneableObject("barrel");
        System.out.println("Object : " + clonedObject2.getName());

        CloneableObject clonedObject3 = ObjectCache.getCloneableObject("largeBox");
        System.out.println("Object : " + clonedObject3.getName());
    }

}
