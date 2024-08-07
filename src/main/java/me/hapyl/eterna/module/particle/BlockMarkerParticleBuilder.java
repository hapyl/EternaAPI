package me.hapyl.eterna.module.particle;

import org.bukkit.Material;
import org.bukkit.Particle;

import javax.annotation.Nonnull;

public class BlockMarkerParticleBuilder extends BlockBreakParticleBuilder {

    BlockMarkerParticleBuilder(@Nonnull Material material) {
        super(material);

        particle = Particle.BLOCK_MARKER;
    }

}
