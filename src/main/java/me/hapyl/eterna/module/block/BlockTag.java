package me.hapyl.eterna.module.block;

import me.hapyl.eterna.module.entity.Experimental;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * Contains tags of blocks with different types.
 */
@Experimental
public final class BlockTag {

    private BlockTag() {
        throw new IllegalStateException();
    }

    /**
     * Includes types of redstone component by type.
     */
    public enum RedstoneComponent {

        BUTTON(Material.OAK_BUTTON, Material.SPRUCE_BUTTON),
        PRESSURE_PLATE(null);

        private final Material[] materials;

        RedstoneComponent(Material... materials) {
            this.materials = materials;
        }

        public Material[] getMaterials() {
            throw new NotImplementedException();
            //            return materials;
        }
    }

    /**
     * Includes corals by type.
     */
    public enum Coral {

        TUBE(
                Material.TUBE_CORAL_BLOCK,
                Material.DEAD_TUBE_CORAL_BLOCK,
                // -- tube coral fans
                Material.TUBE_CORAL,
                Material.TUBE_CORAL_FAN,
                Material.DEAD_TUBE_CORAL,
                Material.DEAD_TUBE_CORAL_FAN
        ),
        BUBBLE(
                Material.BUBBLE_CORAL_BLOCK,
                Material.DEAD_BUBBLE_CORAL_BLOCK,
                // -- bubble coral fans
                Material.BUBBLE_CORAL,
                Material.BUBBLE_CORAL_FAN,
                Material.DEAD_BUBBLE_CORAL,
                Material.DEAD_BUBBLE_CORAL_FAN
        ),
        BRAIN(
                Material.BRAIN_CORAL_BLOCK,
                Material.DEAD_BRAIN_CORAL_BLOCK,
                // -- brain coral fans
                Material.BRAIN_CORAL,
                Material.BRAIN_CORAL_FAN,
                Material.DEAD_BRAIN_CORAL,
                Material.DEAD_BRAIN_CORAL_FAN
        ),
        FIRE(
                Material.FIRE_CORAL_BLOCK,
                Material.DEAD_FIRE_CORAL_BLOCK,
                // -- fire coral fans
                Material.FIRE_CORAL,
                Material.FIRE_CORAL_FAN,
                Material.DEAD_FIRE_CORAL,
                Material.DEAD_FIRE_CORAL_FAN
        ),
        HORN(
                Material.HORN_CORAL_BLOCK,
                Material.DEAD_HORN_CORAL_BLOCK,
                // -- horn coral fans
                Material.HORN_CORAL,
                Material.HORN_CORAL_FAN,
                Material.DEAD_HORN_CORAL,
                Material.DEAD_HORN_CORAL_FAN
        );

        private final Material[] blocks;
        private final Material[] stems;

        Coral(Material... stems) {
            if (stems.length != 6) {
                throw new IllegalArgumentException();
            }
            this.blocks = new Material[2];
            this.stems = new Material[4];

            System.arraycopy(stems, 0, this.blocks, 0, 2);
            System.arraycopy(stems, 2, this.stems, 0, 4);
        }

        /**
         * Returns all corals stems in order: (Coral -> Fan -> Dead Coral -> Dead Coral Fan)
         *
         * @return all corals stems in order: (Coral -> Fan -> Dead Coral -> Dead Coral Fan)
         */
        @Nonnull
        public Material[] getCorals() {
            return stems;
        }

        /**
         * Returns all coral blocks in order: (Coral Block -> Dead Coral Block)
         *
         * @return all coral blocks in order: (Coral Block -> Dead Coral Block)
         */
        public Material[] getCoralBlocks() {
            return blocks;
        }

        /**
         * Returns block of coral.
         *
         * @return block of coral.
         */
        public Material getCoralBlock() {
            return blocks[0];
        }

        /**
         * Returns block of dead coral.
         *
         * @return block of dead coral.
         */
        public Material getCoralDeadBlock() {
            return blocks[1];
        }

        /**
         * Returns coral.
         *
         * @return coral.
         */
        public Material getCoral() {
            return stems[0];
        }

        /**
         * Returns coral's fan.
         *
         * @return coral's fan.
         */
        public Material getCoralFan() {
            return stems[1];
        }

        /**
         * Returns dead coral.
         *
         * @return dead coral.
         */
        public Material getDeadCoral() {
            return stems[2];
        }

        /**
         * Returns dead coral fan.
         *
         * @return dead coral fan.
         */
        public Material getDeadCoralFan() {
            return stems[3];
        }
    }


}
