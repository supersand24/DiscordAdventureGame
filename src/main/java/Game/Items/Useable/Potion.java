package Game.Items.Useable;

import Game.Entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class Potion extends Usable {

     public enum Liquid {
        WATER("Water", 0, 0, null, 0, Status.DYSENTERY, Cleanliness.DIRTY),
        HP("Healing potion", 10, 0, null, 0, null, Cleanliness.CLEAN),
        STAT("Stat up", 0, 3, modifiedStats.values(), 0, null, Cleanliness.OK),
        LVL("Level up potion", 0, 0, null, 0, null, Cleanliness.OK);

        private String name; private int heal, raiseStat, lvlUp; private ArrayList<modifiedStats> stats = new ArrayList<>(5); private Status status; private Cleanliness howClean;
         Liquid(String name, int heal, int raiseStat, modifiedStats[] stats, int lvlUp, Status status, Cleanliness howClean) {
            this.name = name;
            this.heal = heal;
            this.raiseStat = raiseStat;
            this.stats.addAll(List.of(stats));
            this.lvlUp = lvlUp;
            this.status = status;
            this.howClean = howClean;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getHeal() {
            return heal;
        }

        public void setHeal(int heal) {
            this.heal = heal;
        }

        public int getLvlUp() {
            return lvlUp;
        }

        public void setLvlUp(int lvlUp) {
            this.lvlUp = lvlUp;
        }

        public int getRaiseStat() {
            return raiseStat;
        }

        public void setRaiseStat(int raiseStat) {
            this.raiseStat = raiseStat;
        }

        public ArrayList<modifiedStats> getStats() {
            return stats;
        }

        public void setStats(modifiedStats[] stats) {
            this.stats.addAll(List.of(stats));
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Cleanliness getHowClean() {
            return howClean;
        }

        public void setHowClean(Cleanliness howClean) {
            this.howClean = howClean;
        }
    }

    public enum modifiedStats {
        DEF,
        STR,
        DEX,
        SPD,
        WIS,
    }

    public enum Status {
        POISON,
        PAIN,
        HERNIA,
        DYSENTERY,
        PARALYZE,
        SLEEPY,
        HORNY,
        STOP
    }

    public enum Cleanliness {
        KRISP,
        CLEAN,
        OK,
        DIRTY
    }



    private final Liquid liquid;

    public Potion(Potion.Liquid liquid) {
        super (liquid.name, 2, 10);
        this.liquid = liquid;
    }

    public Potion(Potion.Liquid liquid, String name, int heal, int raiseStat, int lvlUp, modifiedStats[] stats, Potion.Status status, Potion.Cleanliness howClean, int cost, int weight) {
        super(name, cost, weight);
        this.liquid = liquid;
        liquid.setName(name);
        liquid.setHeal(heal);
        liquid.setRaiseStat(raiseStat);
        liquid.setLvlUp(lvlUp);
        liquid.setStats(stats);
        liquid.setStatus(status);
        liquid.setHowClean(howClean);
    }

    public Potion() {
        super(Liquid.HP.getName(), 2, 10);
        liquid = Liquid.HP;
        liquid.setHeal(15);
    }


    @Override
    public void use(Entity e) {
        e.setHealth(e.getHealth() + liquid.heal);
        e.setLevel(e.getLevel() + liquid.lvlUp);
        if (liquid.getStats().contains(modifiedStats.DEF)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.WIS)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.DEX)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.SPD)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        if (liquid.getStats().contains(modifiedStats.STR)) {
            e.setDef(e.getDef() + liquid.raiseStat);
        }
        uses--;
    }


    @Override
    public void use() {
        System.out.println("used potion " + liquid.name);
        uses--;
    }

    public Liquid getLiquid() {
        return liquid;
    }
}
