package Game.Items.Useable.Liquids;

import Game.Entities.Entity;

public class Potion extends Liquid {

    protected enum raisedStat {
        HP,
        MAXHP,
        DEF,
        STR,
        DEX,
        SPD,
        WIS,
        LVL

    }

    protected raisedStat statToMod;

    //protected int uses;


    public Potion(String name, Potion.raisedStat statToMod) {
        super(name, 10, 1);
        this.statToMod = statToMod;
    }

    public Potion(Potion.raisedStat statToMod) {
        super();
        this.statToMod = statToMod;
    }

    @Override
    public void use(Entity e) {
        switch (statToMod) {
            case HP -> e.setHealth(e.getHealth()+1);
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
        uses--;
    }
}
