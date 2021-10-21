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



    public Potion(String name, Potion.raisedStat statToMod) {
        super(name, 10, 1);
        this.statToMod = statToMod;
    }

    public Potion(Potion.raisedStat statToMod) {
        super();
        this.statToMod = statToMod;
    }

    public Potion() {
        super();
        statToMod = raisedStat.HP;
    }

    @Override
    public void use(Entity e) {
        switch (statToMod) {
            case HP -> e.setHealth(e.getHealth()+10);
            case MAXHP -> e.setMaxHealth(e.getMaxHealth()+1);
            case DEF -> e.setDef(e.getDef()+1);
            case STR -> e.setStr(e.getStr()+1);
            case DEX -> e.setDex(e.getDex()+1);
            case SPD -> e.setSpd(e.getSpd()+1);
            case WIS -> e.setWis(e.getWis()+1);
            case LVL -> e.setLevel(e.getLevel()+1);
        }
        uses--;
    }

    @Override
    public void use() {
        System.out.println("used potion");
        uses--;
    }
}
