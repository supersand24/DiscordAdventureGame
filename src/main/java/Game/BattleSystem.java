package Game;

import Game.Entities.EnemyTypes.Enemy;
import Game.Entities.Entity;
import Game.Entities.Player;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.*;

/**
 * All Battle System methods for the game (includes some static utilities as well)
 * @author Harrison Brown
 * @author Justin Sandman
 * @version 1.0
 */
public class BattleSystem {

    /**
     * enum for the actions that can be taken in a turn
     */
    public enum actions {
        ATTACK,
        BLOCK
    }

    /**
     * list of all parties engaged in battle
     */
    private static List<Party> activeBattles = new ArrayList<>();

    /**
     * starts battle
     * @author Harrison Brown
     */
    public static void startBattle(Party p) {
        //adds the party to the list of active battles
        activeBattles.add(p);

        //temp array of players
        Player[] players = p.getPlayers(Game.guild).toArray(new Player[0]);

        //sets the turn order in the party
        makeTurnOrder(p);

        //iterate through turn order if enemy do turn, if player set party.turn = first player in turn order list
        processTurn(p,0);
    }

    /**
     * ends battle
     * @author Justin Sandman
     */
    public static void endBattle(Party p) {
        //removes the party from the list of active battles
        p.enemies.clear();
        activeBattles.remove(p);
        p.battleMessage = null;

        TextChannel channel = Game.guild.getTextChannelById(p.channelId);
        if (channel != null) {
            channel.sendMessage("The battle is over, stop and heal up.").queue();
        }

        //Send Battle Log

        //EXP Handling
        for (Player player : p.getPlayers(Game.guild)) {
            //add xp
            //Check for levelups
        }
    }

    /**
     * handles the current set of turns
     * @author Harrison Brown
     * @param p a party
     * @param cnt the starting index in the turn order
     */
    public static void processTurn(Party p, int cnt) {

        for (int i = cnt; i < p.getTurnOrder().length; i++) {
            if (p.getTurnOrder()[i] instanceof Enemy && p.getTurnOrder()[i].getIsAlive()) {
                enemyTurn(p, (Enemy)p.getTurnOrder()[i]);
            }

            if (p.getTurnOrder()[i] instanceof Player) {
                p.setTurnIndex(i);
                break;
            }

        }
    }

    /**
     * computer takes turn
     * @author Harrison Brown
     */
    private static void enemyTurn(Party party, Enemy e) {
        Player[] players = party.getPlayers(Game.guild).toArray(new Player[0]);
        int act;
        act = randomVal(0, 1);
        //act = 1;
        if (act == 0) {
            if (players.length == 1) {
                attack(e, players[0]);
                if (!players[0].getIsAlive()) {
                    //System.out.println(players[0].getName() + " You died");
                }
            } else {
                int p;
                do {
                    p = randomVal(0, players.length - 1);
                } while(!players[p].getIsAlive());

                attack(e, players[p]);
                if (!players[p].getIsAlive()) {
                    //System.out.println(players[p].getName() + "You died");
                }
            }
            //System.out.println();

        } else {
            block(e);
            //System.out.println();
        }
    }

    /**
     * checks if the given member's turn is now
     * @author Harrison Brown
     * @param p the party the member is from
     * @param m the member to know if it's their turn
     * @return boolean
     */
    public static boolean isTurn(Party p, Member m) {
        if (p.getTurnOrder() != null) {
            return p.getTurnOrder()[p.getTurnIndex()].equals(p.getPlayer(m));
        }
        return false;
    }

    /**
     * sets the turnOrder array in the party class based on the speed of the entities in the encounter
     * @author Harrison Brown
     */
    private static void makeTurnOrder(Party p) {
        Entity[] arr1;
        Entity[] arr2;
        Entity[] order;

        arr1 = p.getPlayers(Game.guild).toArray(new Entity[0]);
        arr2 = p.enemies.toArray(new Entity[0]);

        order = new Entity[arr1.length + arr2.length];

        int i = 0;

        for (Entity entity : arr1) {
                order[i] = entity;
                i++;
        }

        for (Entity entity : arr2) {
            order[i] = entity;
            i++;
        }

        Arrays.sort(order);
        p.setTurnOrder(order);

    }

    /**
     * Converts decision from discord to action.
     *
     * @author Justin Sandman
     * @param choice a string representation of the choice the entity wants to make
     * @param slashCommand the command info from discord, can get player, party, and member
     */
    public static void makeChoice(BattleSystem.actions choice, SlashCommandEvent slashCommand) {

        Member member = slashCommand.getMember();

        Party party = null;
        for (Party p : activeBattles) {
            if (p.getMembers(Game.guild).contains(member)) {
                party = p;
            }
        }

        if (party != null && member != null) {

            if (isTurn(party, member)) {

                slashCommand.deferReply(true).queue();

                Player player = party.getPlayer(member);

                switch (choice) {
                    case ATTACK -> attack(
                            player,
                            party.enemies.get(
                                    (int) slashCommand.getOption("target").getAsLong() - 1
                            ));
                    case BLOCK -> block(player);
                }
                
                slashCommand
                        .getHook()
                        .sendMessage(player.getLastAction())
                        .queue();

                processTurn(party, party.getTurnIndex() + 1);

                party.sendBattleMessage();

                if ( !isOneEntityAlive(party.enemies.toArray(new Entity[0])) ) {
                    endBattle(party);
                }

            } else {
                slashCommand
                        .getHook()
                        .sendMessage("It's not your turn.")
                        .queue();
            }
        }
    }

    /**
     * General attack method
     *
     * @author Justin Sandman
     * @param self the attacking user
     * @param target the target the user is trying to hit
     */
    public static void attack(Entity self, Entity target) {
        self.attack(target);
        target.checkHealth();
    }

    /**
     * General block method
     *
     * @author Justin Sandman
     * @param self the user bracing
     */
    public static void block(Entity self) {
        self.block();
    }

    /**
     * Checks list of entities if at least one of them is alive.
     *
     * @author Harrison Brown
     * @author Justin Sandman
     * @param entities usually the enemy list
     * @return true if one is alive
     */
    public static boolean isOneEntityAlive(Entity[] entities) {
        for (Entity entity : entities) {
            if (entity.getHealth() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * utility to return a random value
     * @author Harrison Brown
     * @param min minimum bound
     * @param max maximum bound
     * @return the generated random value
     */
    public static int randomVal(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(min, max);
    }
}
