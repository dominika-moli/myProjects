package sk.tuke.kpi.oop.game.prototype;

import sk.tuke.kpi.oop.game.Barrel;
import sk.tuke.kpi.oop.game.LargeBox;
import sk.tuke.kpi.oop.game.characters.ArmedAlien;

import java.util.Hashtable;

public class ObjectCache {

    private  static Hashtable<String, CloneableObject> cloneableObjectMap;

    public ObjectCache(){
        cloneableObjectMap = new Hashtable<>();
    }

    public static CloneableObject getCloneableObject(String objectName) {
        CloneableObject cachedObject = cloneableObjectMap.get(objectName);
        return (CloneableObject) cachedObject.clone();
    }

    public static void loadCache() {
        ArmedAlien armedAlien = new ArmedAlien();
        cloneableObjectMap.put(armedAlien.getName(), armedAlien);

        Barrel barrel = new Barrel();
        cloneableObjectMap.put(barrel.getName(), barrel);


        LargeBox largeBox = new LargeBox();
        cloneableObjectMap.put(largeBox.getName(), largeBox);
    }

}
