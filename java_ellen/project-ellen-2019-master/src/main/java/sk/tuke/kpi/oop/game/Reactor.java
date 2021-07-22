package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

import java.util.HashSet;
import java.util.Set;

public class Reactor extends AbstractActor implements Switchable, Repairable {
    private int temperature;
    private int damage;
    private Animation reactorOffAnimation;
    private Animation normalAnimation;
    private Animation hotAnimation;
    private Animation brokenAnimation;
    private Animation reactorExtinguishedAnimation;
    private boolean isOn;

     private Set<EnergyConsumer> devices;

    public Reactor() {
        temperature = 0;
        isOn =false;
        devices = new HashSet<>();
        damage = 0;
        reactorOffAnimation = new Animation("sprites/reactor.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        normalAnimation = new Animation("sprites/reactor_on.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        hotAnimation = new Animation("sprites/reactor_hot.png", 80, 80, 0.05f);
        brokenAnimation = new Animation("sprites/reactor_broken.png", 80, 80, 0.01f);
        reactorExtinguishedAnimation = new Animation("sprites/reactor_extinguished.png",80,80);
        setAnimation(reactorOffAnimation);
    }

    public int getTemperature() {
        return temperature;
    }

    public int getDamage() { return damage; }

    public void increaseTemperature(int increment) {
        if (isOn && increment >=0) {
            if (damage >= 33 && damage <= 66) {
                temperature = (int) (temperature + (1.5 * increment));
            } else if (damage > 66) {
                temperature = temperature + (2 * increment);
            } else {
                temperature = temperature + increment;
            }

            if (temperature > 2000) {
                int cur_temperature = temperature - 2000;
              //  this.damage = (int)((cur_temperature/(float)4000)*100);
                this.damage = Math.round((100*cur_temperature) / 4000);
            }

            if (damage >= 100) {
                damage = 100;
                isOn= false;
            }
            updateAnimation();
        }
    }
    public void decreaseTemperature ( int decrement) {
            if ((isOn) && (decrement >= 0)) {
                if (damage == 100) {
                 updateAnimation();
                } else if (damage >= 50) {
                     temperature = (int) (temperature - (decrement * 0.5));
                     updateAnimation();
                 } else {
                     temperature = temperature - decrement;
                     updateAnimation();
                 }
                if (temperature < 0) {
                    temperature = 0;
                }

             }
    }
    private void updateAnimation(){
        if (!isOn) {
            if (damage < 100) {
                setAnimation(reactorOffAnimation);
            } else {
                setAnimation(brokenAnimation);
            }
        } else if (temperature < 0) {
            return;
        } else if (temperature <= 4000) {
            setAnimation(normalAnimation);
        } else if (temperature < 6000){
            setAnimation(hotAnimation);
        } else {
            setAnimation(brokenAnimation);
        }
        updateAllDevices();
       }



    public boolean repair() {
        if ((damage > 0) && (damage < 100)) {
            temperature = ((damage - 50) * 40) + 2000;
            if (damage > 50) {
                damage = damage - 50;
                updateAnimation();
            } else {
                damage = 0;
              updateAnimation();
            }
            return true;
        }
        return false;
    }

    public void turnOn() {
       if (this.getDamage() >= 100) {isOn=false;
          } else {
            isOn = true;
          updateAnimation();
        }
    }

    public void turnOff() {
        if (temperature >= 6000) {
            setAnimation(brokenAnimation);
           } else {
            setAnimation(reactorOffAnimation);
        }
        isOn = false;
        //setElectricityInDevices(false);
    }


     public boolean isOn() {
        if (isOn) {
            return true;
        } else return false;
    }

    public void addDevice(EnergyConsumer device) {
        this.devices.add(device);
        if (damage == 0 && isOn() ) {
            device.setPowered(true);
        }
        device.setPowered(isOn());

    }

    public void removeDevice(EnergyConsumer device){
        device.setPowered(false);
        this.devices.remove(device);

    }

    public boolean extinguish(){
        if ( damage == 0  || !isOn) {
            return false;
        }
        else {
            this.temperature =  this.getTemperature() - 4000;
            setAnimation(reactorExtinguishedAnimation);
            return true;
        }
    }

    private boolean isElectricity(){
        return isOn() && damage != 100;
    }

    private void updateStateOfDevice(EnergyConsumer device){
        device.setPowered(isElectricity());
    }

    private void updateAllDevices(){
        this.devices.forEach(this::updateStateOfDevice);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new PerpetualReactorHeating(1).scheduleFor(this);


    }



}
