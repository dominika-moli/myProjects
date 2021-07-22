package sk.tuke.kpi.oop.game.weapons;

public class LaserGun extends Firearm {

    public LaserGun(int initialAmmo, int maxAmmo) {
        super(initialAmmo, maxAmmo);
    }

    @Override
    protected Fireable createBullet() {
        return new Laser();
    }
}
