package Game.Items.Weapons;

/**
 * Sword weapon class
 * @author Harrison Brown
 * @version 0.1
 */
public class Sword extends Weapon {
    /**
     * constructor to create a sword with the given name
     * @param name The name of the Sword
     */
    public Sword(String name) {
        super(name,20, 15, 10 );
    }

    public void attack() {
        System.out.println("You attacked with " + name);
    }

}
