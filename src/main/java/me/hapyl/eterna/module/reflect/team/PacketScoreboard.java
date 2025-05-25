package me.hapyl.eterna.module.reflect.team;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public class PacketScoreboard extends Scoreboard {
    
    protected boolean teamChanged;
    
    PacketScoreboard() {
        this.teamChanged = false;
    }
    
    @Override
    public void onTeamChanged(PlayerTeam var0) {
        this.teamChanged = true;
    }
}
