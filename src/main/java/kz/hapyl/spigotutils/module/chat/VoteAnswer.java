package kz.hapyl.spigotutils.module.chat;

import org.bukkit.entity.Player;

import java.util.Set;

public class VoteAnswer {

    private final int index;
    private final String answer;
    private final Set<Player> players;

    public VoteAnswer(int index, String answer, Set<Player> players) {
        this.index = index;
        this.answer = answer;
        this.players = players;
    }

    public void addVote(Player player) {
        this.players.add(player);
    }

    public void removeVote(Player player) {
        this.players.remove(player);
    }

    public int getIndex() {
        return index;
    }

    public String getAnswer() {
        return answer;
    }

    public Set<Player> getPlayers() {
        return players;
    }

}
