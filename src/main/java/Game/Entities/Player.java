package Game.Entities;

import Game.Items.Weapons.Sword;

/**
 * Player class
 * @author Harrison Brown
 * @version 0.1
 */
public class Player extends Entity {
    /**
     * The name of the character
     */
    protected final String name;

    /**
     * Gender of the character
     */

    protected String gender;

    /**
     * player constructor
     * @param gold initial gold
     * @param maxHealth initial max health
     * @see Game.Entities.Entity
     */
    public Player(int gold, int maxHealth, String weaponName, String weaponOwner, String playerName, String gender) {
        super(gold, maxHealth);
        holding[0] = new Sword(weaponName, weaponOwner);
        this.name = playerName;
        this.gender = gender;
    }

    @Override
    public void attack(Entity entity) {
        entity.setHealth(entity.getHealth()-holding[0].getDmg());
    }

    @Override
    public void move() {
    }

    @Override
    public void block() {
    }

    @Override
    public void useItem() {
    }
}
