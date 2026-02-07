package me.hapyl.eterna.module.util;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.ForceLowercase;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Represents a class that allows measuring code execution on different steps in nanoseconds.
 */
public class NanoBenchmark {
    
    private final Map<String, Long> steps;
    private final long start;
    
    /**
     * Creates a new {@link NanoBenchmark} and starts it immediately.
     */
    public NanoBenchmark() {
        this.steps = Maps.newLinkedHashMap();
        this.start = System.nanoTime();
    }
    
    /**
     * Records a step by the given name with the current {@link System#nanoTime()}.
     *
     * @param name - The name of the step.
     */
    public void step(@NotNull @ForceLowercase String name) {
        this.steps.put(name.toLowerCase(), System.nanoTime());
    }
    
    /**
     * Gets a {@link List} of {@link Result}, or an empty {@link List} list if there are no steps recorded.
     *
     * @return a list of results.
     */
    @NotNull
    public List<Result> getResults() {
        if (steps.isEmpty()) {
            return List.of();
        }
        
        return steps.entrySet()
                    .stream()
                    .map(entry -> new Result(entry.getKey(), entry.getValue()))
                    .toList();
    }
    
    /**
     * Gets the first {@link Result} from this {@link NanoBenchmark}.
     *
     * @return The first result from the benchmark.
     */
    @NotNull
    public Optional<Result> getFirstResult() {
        final List<Result> results = getResults();
        
        return results.isEmpty() ? Optional.empty() : Optional.ofNullable(results.getFirst());
    }
    
    /**
     * Gets a {@link String} representation of this {@link NanoBenchmark}, listing the name and time for each steps in milliseconds.
     *
     * @return a string representation of this benchmark.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        
        getResults().forEach(result -> {
            builder.append("'%s' took %sms.".formatted(result.name, result.asMillis()));
            builder.append("\n");
        });
        
        return builder.toString();
    }
    
    /**
     * A static factory method for creating {@link NanoBenchmark} at this instant.
     *
     * @return a new nano benchmark.
     */
    @NotNull
    public static NanoBenchmark ofNow() {
        return new NanoBenchmark();
    }
    
    /**
     * Represents a {@link Result} for {@link NanoBenchmark}, consisting of the name of the step
     * and the time it took to complete that step.
     */
    public static final class Result extends Duration {
        
        private final String name;
        
        private Result(@NotNull String name, long value) {
            super(value, TimeUnit.NANOSECONDS);
            
            this.name = name;
        }
        
        /**
         * Gets the name of the step.
         *
         * @return The name of the step.
         */
        @NotNull
        public String name() {
            return name;
        }
        
        /**
         * Gets a {@link String} representation of the {@link Result}.
         *
         * @return a string representing the result.
         */
        @Override
        public String toString() {
            return "%s=%sms".formatted(name, asMillis());
        }
    }
    
}
