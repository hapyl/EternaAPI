package me.hapyl.eterna.module.chat.messagebuilder;

import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.function.BiConsumer;

public enum Format implements BiConsumer<ComponentBuilder, Boolean> {

    /**
     * Makes the text bold.
     */
    BOLD {
        @Override
        public void accept(ComponentBuilder componentBuilder, Boolean aBoolean) {
            componentBuilder.bold(aBoolean);
        }
    },

    /**
     * Makes the text italic.
     */
    ITALIC {
        @Override
        public void accept(ComponentBuilder componentBuilder, Boolean aBoolean) {
            componentBuilder.italic(aBoolean);
        }
    },

    /**
     * Makes the text underlined.
     */
    UNDERLINED {
        @Override
        public void accept(ComponentBuilder componentBuilder, Boolean aBoolean) {
            componentBuilder.underlined(aBoolean);
        }
    },

    /**
     * Makes the text strikethrough.
     */
    STRIKETHROUGH {
        @Override
        public void accept(ComponentBuilder componentBuilder, Boolean aBoolean) {
            componentBuilder.strikethrough(aBoolean);
        }
    },

    /**
     * Makes the text obfuscated.
     */
    OBFUSCATED {
        @Override
        public void accept(ComponentBuilder componentBuilder, Boolean aBoolean) {
            componentBuilder.obfuscated(aBoolean);
        }
    }


}
