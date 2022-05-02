package kz.hapyl.spigotutils.module.command.jcmd.argument;

public interface Argument<T> {

	boolean validate(T in);

}
