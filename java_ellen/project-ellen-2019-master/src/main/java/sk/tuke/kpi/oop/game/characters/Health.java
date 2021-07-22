package sk.tuke.kpi.oop.game.characters;

import java.util.ArrayList;
import java.util.List;

public class Health {
    private int currentHealth;
    private int maxHealth;
    private List<ExhaustionEffect> efects;

    public Health(int initialHealth, int maxHealth){
        this.currentHealth = initialHealth;
        this.maxHealth = maxHealth;
        efects = new ArrayList<>();
       }

    public Health(int initialHealth) {
        this.currentHealth =initialHealth;
        maxHealth=this.currentHealth;
    }

    public int getValue() {
        return this.currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void refill (int amount) {
        if (currentHealth +amount<=maxHealth)
            currentHealth +=amount;
        else
            restore();
    }

    public void restore() {
        currentHealth =maxHealth;
    }

    public void drain (int amount) {
        if (currentHealth !=0) {
            if (currentHealth >amount)
                currentHealth -= amount;
            else
                exhaust();
        }
    }

    public void exhaust() {
        if (currentHealth != 0) {
            currentHealth = 0;

            if (efects != null) {
                efects.forEach(ExhaustionEffect::apply);
            }
        }
    }

    @FunctionalInterface
    public interface ExhaustionEffect {
        void apply();
    }

    public void onExhaustion(ExhaustionEffect effect)
    {
        if (efects!=null)
            efects.add(effect);

    }
}
