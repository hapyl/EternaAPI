package me.hapyl.eterna.builtin.command;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.builtin.menu.QuestJournal;
import me.hapyl.eterna.builtin.updater.UpdateResult;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.command.SimpleAdminCommand;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.StringList;
import me.hapyl.eterna.module.util.TypeConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import test.EternaTest;
import test.EternaTestRegistry;

import java.util.List;

@ApiStatus.Internal
public final class EternaCommand extends SimpleAdminCommand {
    
    public EternaCommand(@NotNull EternaKey key, @NotNull String name) {
        super(name);
        
        key.validateKey();
        
        setDescription("API management administrative command.");
    }
    
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
        if (args.length == 0) {
            this.sendCommandUsage(sender);
        }
        
        final Operation operation = args.get(0).toEnum(Operation.class);
        
        if (operation == null) {
            EternaLogger.message(sender, Component.text("Invalid operation!", NamedTextColor.RED));
            return;
        }
        
        operation.execute(sender, args.copyOfRange(1));
    }
    
    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull ArgumentList args) {
        if (args.length == 1) {
            return StringList.ofEnumConstantLowercaseNames(Operation.class);
        }
        else if (args.length >= 2) {
            final Operation operation = args.get(0).toEnum(Operation.class);
            
            return operation != null ? operation.tabComplete(args.copyOfRange(1)) : List.of();
        }
        
        return List.of();
    }
    
    public enum Operation {
        
        UPDATE {
            @Override
            public void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
                final Updater updater = Eterna.getUpdater();
                
                updater.checkForUpdates().thenAccept(response -> {
                    EternaLogger.message(sender, response.result().asComponent());
                    
                    if (response.result() == UpdateResult.OUTDATED) {
                        EternaLogger.message(sender, response.downloadUrl());
                    }
                });
            }
        },
        
        QUEST {
            @Override
            public void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
                if (!(sender instanceof Player player)) {
                    EternaLogger.message(sender, Component.text("Only players may complete quests!", NamedTextColor.RED));
                }
                else if (!Eterna.getConfig().allowQuestJournal()) {
                    EternaLogger.message(player, Component.text("This server does not allow Quest Journal!", NamedTextColor.RED));
                }
                else {
                    new QuestJournal(player);
                }
            }
        },
        
        RELOAD {
            @Override
            public void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
                EternaLogger.message(sender, Component.text("Reloading config...", NamedTextColor.YELLOW));
                
                Eterna.getConfig().reload()
                      .thenRun(() -> {
                          EternaLogger.message(sender, Component.text("Successfully reloaded config!", NamedTextColor.GREEN));
                      })
                      .exceptionally(ex -> {
                          EternaLogger.message(
                                  sender,
                                  Component.text("Error reloading config! ", NamedTextColor.DARK_RED).append(Component.text(ex.getCause().getMessage(), NamedTextColor.YELLOW))
                          );
                          
                          return null;
                      });
            }
        },
        
        TEST {
            @Override
            public void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
                if (!Eterna.getConfig().keepTestCommands()) {
                    EternaLogger.message(sender, Component.text("Tests are disabled on this server!", NamedTextColor.RED));
                    return;
                }
                
                if (true) {
                    EternaLogger.debug(EternaTestRegistry.listTests());
                }
                
                final String testStringKey = args.get(0).toString();
                final Key testKey = Key.ofStringOrNull(testStringKey);
                
                if (testKey == null) {
                    EternaLogger.message(sender, Component.text("Invalid key format, must match `%s` pattern!".formatted(Key.PATTERN.pattern()), NamedTextColor.RED));
                    return;
                }
                
                final EternaTest test = EternaTestRegistry.get(testKey);
                
                if (test == null) {
                    EternaLogger.message(sender, Component.text("Invalid test `%s`!".formatted(testKey), NamedTextColor.RED));
                    return;
                }
                
                if (!(sender instanceof Player player)) {
                    EternaLogger.message(sender, Component.text("Only players can execute tests!", NamedTextColor.RED));
                    return;
                }
                
                test.test0(player, args.copyOfRange(1));
            }
            
            @NotNull
            @Override
            public List<String> tabComplete(@NotNull ArgumentList args) {
                if (args.length == 1) {
                    return EternaTestRegistry.listTests();
                }
                
                return List.of();
            }
        };
        
        public void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
            throw new IllegalStateException();
        }
        
        @NotNull
        public List<String> tabComplete(@NotNull ArgumentList args) {
            return List.of();
        }
        
    }
    
}