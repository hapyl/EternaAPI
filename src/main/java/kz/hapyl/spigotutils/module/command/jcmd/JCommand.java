package kz.hapyl.spigotutils.module.command.jcmd;

import kz.hapyl.spigotutils.module.command.SimpleCommand;
import kz.hapyl.spigotutils.module.command.jcmd.argument.Argument;
import kz.hapyl.spigotutils.module.command.jcmd.argument.Literal;
import kz.hapyl.spigotutils.module.command.jcmd.argument.Type;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class JCommand extends SimpleCommand implements CommandBuilder {

	private final ArrayList<CommandArgument<?>> arguments;

	public JCommand(String command) {
		super(command);
		this.arguments = new ArrayList<>();
	}

	@Override
	protected final void execute(CommandSender sender, String[] args) {

	}

	@Override
	public <T> CommandArgument<T> accepts(Argument<T> t) {
		return new CommandArgument<>(this);
	}

	public CommandArgument<String> accepts(String literal) {
		return this.accepts(new Literal(literal));
	}

	public <T> CommandArgument<Class<?>> accepts(Class<T> type) {
		return this.accepts(new Type<>(type));
	}

}
