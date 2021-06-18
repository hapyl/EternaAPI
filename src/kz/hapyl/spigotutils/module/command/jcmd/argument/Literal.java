package kz.hapyl.spigotutils.module.command.jcmd.argument;

public class Literal implements Argument<String> {

	protected final String literal;
	private final boolean ignoreCase;

	public Literal(String literal, boolean ignoreCase) {
		this.literal = literal;
		this.ignoreCase = ignoreCase;
	}

	public Literal(String literal) {
		this(literal, true);
	}

	@Override
	public boolean validate(String in) {
		return this.ignoreCase ? in.equalsIgnoreCase(this.literal) : in.equals(this.literal);
	}
}
