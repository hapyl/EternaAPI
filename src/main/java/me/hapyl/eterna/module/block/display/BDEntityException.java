package me.hapyl.eterna.module.block.display;

/**
 * Runtime exception for {@link BDEngine}.
 */
class BDEntityException extends IllegalArgumentException {
    
    public BDEntityException(String s) {
        super("Unable to parse Block Display model: " + s);
    }
}
