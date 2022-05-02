package test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		splitString("This is a test string to split after a set character limit.", 14);
	}

	@Nonnull
	public static List<String> splitString(String str, int limit) {
		List<String> strings = new ArrayList<>();

		/**
		 * 1. Split the string into characters.
		 * 2. Iterate every character.
		 * 		-> If in limit and hit white space = split.
		 * 	    -> Else split before the word.
		 */

		final char[] chars = str.toCharArray();

		int now = 0;
		StringBuilder builder = new StringBuilder();

		for (final char c : chars) {
			if (Character.isWhitespace(c)) {
				strings.add(builder.toString());
				builder = new StringBuilder();
				continue;
			}

			++now;
			builder.append(c);
		}

		return strings;
	}


}
