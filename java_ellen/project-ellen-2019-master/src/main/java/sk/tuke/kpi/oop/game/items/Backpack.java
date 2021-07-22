package sk.tuke.kpi.oop.game.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.ActorContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Backpack implements ActorContainer<Collectible> {
    private final String name;
    private final int capacity;
    private List<Collectible> vBatohu = new ArrayList<>();


    public Backpack(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public @NotNull List<Collectible> getContent() {
        return List.copyOf(vBatohu);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getSize() {
        return vBatohu.size();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void add(@NotNull Collectible actor) {
        if (vBatohu.size() < getCapacity()) {
            vBatohu.add(actor);
            }
        else  {
            throw new IllegalStateException(getName()+" is full");
        }
    }

    @Override
    public void remove(@NotNull Collectible actor) {
        if (vBatohu !=null)
            vBatohu.remove(actor);
       }

    @NotNull
    @Override
    public Iterator<Collectible> iterator() {
        return vBatohu.iterator();
    }

    @Nullable
    @Override
    public Collectible peek() {
        if (getSize()>0) {
            return vBatohu.get(getSize()-1);
        }
        else {
            return null;
        }
    }

    @Override
    public void shift() {
        Collections.rotate(vBatohu, 1);
    }


}
