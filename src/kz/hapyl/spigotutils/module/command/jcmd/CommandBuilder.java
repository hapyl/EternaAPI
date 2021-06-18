package kz.hapyl.spigotutils.module.command.jcmd;

import kz.hapyl.spigotutils.module.command.jcmd.argument.Argument;

public interface CommandBuilder {

	// new UXCommand("help).accepts("me").or("them").with().accepts()

	/**
	 * new UXCommand("admin").accepts(Integer).accepts()
	 */

	<T> CommandArgument<T> accepts(Argument<T> t);

}
