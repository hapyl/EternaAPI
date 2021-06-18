package kz.hapyl.spigotutils.module.chat;

import org.bukkit.entity.Player;

import java.util.*;

public class Vote {

	protected static final Set<Vote> storedVotes = new HashSet<>();

	private final String           question;
	private final long             timeBeing;
	private final List<VoteAnswer> answers;

	public Vote(String question, long timeBeing) {
		this.question = question;
		this.timeBeing = timeBeing;
		this.answers = new ArrayList<>();
	}

	public void addVote(Player player, int index) {
		if (index >= answers.size()) {
			Chat.sendFormat(player, "&cThere is no answer at {index}!", index);
			return;
		}

		final VoteAnswer answer = answers.get(index);
		answer.addVote(player);
		Chat.sendFormat(player, "&aYou have voted for {answer} for {question}.", answer.getAnswer(), this.question);

	}

	public String getQuestion() {
		return question;
	}

	public long getTimeBeing() {
		return timeBeing;
	}

	public List<VoteAnswer> getAnswers() {
		return answers;
	}

}
