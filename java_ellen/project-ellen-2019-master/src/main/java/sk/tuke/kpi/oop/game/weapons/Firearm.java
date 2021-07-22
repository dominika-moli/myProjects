package sk.tuke.kpi.oop.game.weapons;

public abstract class Firearm {
    private int currentAmmo;
    private int maxAmmo;


    public Firearm(int initialAmmo, int maxAmmo) {
       this.currentAmmo = initialAmmo;
        this.maxAmmo=maxAmmo;
    }

    public Firearm(int initialAmmo) {
        this.currentAmmo = initialAmmo;
        this.maxAmmo = initialAmmo;
        }

    public int getAmmo() {
        return currentAmmo;
    }
    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void decreaseAmmo(int amount) {
        if (currentAmmo != 0) {
            currentAmmo = currentAmmo - amount;
        }
        if (currentAmmo < 0) {
            currentAmmo = 0;
        }
    }

    public void reload(int newAmmo) {
        if (getAmmo() + newAmmo < maxAmmo) {
            currentAmmo += newAmmo;
        }
        else {
            currentAmmo = maxAmmo;
        }
    }

    protected abstract Fireable createBullet();

    public Fireable fire() {
        if (currentAmmo != 0) {
            currentAmmo--;
            return createBullet();
        }
        else {
            return null;
        }
    }
}
