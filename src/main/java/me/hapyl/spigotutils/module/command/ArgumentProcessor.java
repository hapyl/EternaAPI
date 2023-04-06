package me.hapyl.spigotutils.module.command;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.annotate.ArgumentSensitive;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Dynamically processes all methods with {@link ArgumentSensitive} annotation.
 *
 * @apiNote Though seemed like a good idea, this class is not used in the plugin.
 * @deprecated Not removing this, but not handling either.
 */
@Deprecated()
public class ArgumentProcessor {

    private final SimpleCommand command;
    private final Set<String> superNames;
    private final Map<ArgumentSensitive, Method> argumentMap;

    public ArgumentProcessor(SimpleCommand command) {
        this.command = command;
        this.superNames = Sets.newHashSet();
        this.argumentMap = Maps.newHashMap();

        for (Method method : SimpleCommand.class.getDeclaredMethods()) {
            superNames.add(method.getName());
        }

        validateMethods();
    }

    private void validateMethods() {
        for (Method method : command.getClass().getDeclaredMethods()) {
            final ArgumentSensitive annotation = method.getAnnotation(ArgumentSensitive.class);
            final String methodName = method.getName();
            if (annotation == null || superNames.contains(methodName)) {
                continue;
            }

            final Class<?>[] types = method.getParameterTypes();

            if (types.length != 2) {
                throw new IllegalArgumentException("Method '%s' needs to have two arguments, not %s!".formatted(methodName, types.length));
            }
            else if (types[0] != CommandSender.class) {
                throw new IllegalArgumentException("Method '%s' has '%s' as it's first argument but must have 'CommandSender'!".formatted(
                        methodName,
                        types[0].getSimpleName()
                ));
            }
            else if (types[1] != String[].class) {
                throw new IllegalArgumentException("Method '%s' has '%s' as it's second argument but must have 'String[]'!".formatted(
                        methodName,
                        types[1].getSimpleName()
                ));
            }

            argumentMap.put(annotation, method);
        }

    }

    public void checkArgumentAndExecute(CommandSender sender, String[] args) {
        argumentMap.forEach((argument, method) -> {
            final int size = argument.targetSize();
            final String string = argument.targetString();
            if (Validate.checkArrayString(args, size, string)) {
                try {
                    method.invoke(command, sender, args);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        });
    }

    /**
     * Returns how many methods can potentially exist.
     * Though does not test how valid methods are, just
     * the presence of annotation. {@link ArgumentSensitive}
     */
    public static int countMethods(SimpleCommand command) {
        int methods = 0;
        for (Method method : command.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(ArgumentSensitive.class) != null) {
                methods++;
            }
        }
        return methods;
    }

    public Map<ArgumentSensitive, Method> getArgumentMap() {
        return argumentMap;
    }

    public SimpleCommand getCommand() {
        return command;
    }
}
