package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.ForceLowercase;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A utility class for benchmarking execution time of different steps.
 * <br>
 * It records the time at the start and for each step, and provides methods to retrieve the results.
 */
public class NanoBenchmark {
    
    private final Map<String, Long> steps;
    private Long start;
    
    /**
     * Constructs a new NanoBenchmark instance with an empty map of steps.
     */
    public NanoBenchmark() {
        this.steps = Maps.newLinkedHashMap();
    }
    
    /**
     * Creates a new instance of {@link NanoBenchmark} and starts it immediately.
     *
     * @return A new {@link NanoBenchmark} instance that has been started.
     */
    @Nonnull
    public static NanoBenchmark ofNow() {
        final NanoBenchmark nanoBenchmark = new NanoBenchmark();
        nanoBenchmark.start();
        
        return nanoBenchmark;
    }
    
    /**
     * Starts the benchmark by recording the current system nano time.
     * <br>
     * If the benchmark has already been started, this method does nothing.
     */
    public void start() {
        if (this.start != null) {
            return;
        }
        
        this.start = System.nanoTime();
    }
    
    /**
     * Records a step with the given name and the current system nano time.
     *
     * @param name - The name of the step.
     */
    public void step(@Nonnull @ForceLowercase String name) {
        this.steps.put(name.toLowerCase(), System.nanoTime());
    }
    
    /**
     * Returns the list of results, each representing the time taken for a step relative to the start time.
     *
     * @return A list of {@link Result} objects representing each step.
     * @throws IllegalStateException If the benchmark was not started or no steps have been recorded.
     */
    @Nonnull
    public List<Result> getResult() {
        Validate.isTrue(start != null, "The benchmark was not started!");
        Validate.isTrue(!steps.isEmpty(), "The benchmark was not stepped!");
        
        final List<Result> results = Lists.newArrayList();
        steps.forEach((name, result) -> results.add(new Result(name, result - start)));
        
        return results;
    }
    
    /**
     * Returns the first result from the benchmark.
     *
     * @return The first {@link Result} from the benchmark.
     * @throws IllegalStateException If the benchmark was not started or no steps have been recorded.
     */
    @Nonnull
    public Result getFirstResult() {
        return getResult().getFirst();
    }
    
    /**
     * Returns a string representation of the benchmark results, listing the name and time for each step.
     *
     * @return A formatted string representing the benchmark results.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        
        getResult().forEach(result -> {
            builder.append("'%s' took %sms.".formatted(result.name, result.asMillis()));
            builder.append("\n");
        });
        
        return builder.toString();
    }
    
    /**
     * Represents the result of a step in the benchmark, including the name of the step
     * and the duration it took.
     */
    public static final class Result extends Duration {
        
        private final String name;
        
        private Result(@Nonnull String name, long value) {
            super(value, TimeUnit.NANOSECONDS);
            this.name = name;
        }
        
        /**
         * Returns the name of the step associated with this result.
         *
         * @return The name of the step.
         */
        @Nonnull
        public String name() {
            return name;
        }
        
        /**
         * Returns a string representation of the result, formatted as the name and duration in milliseconds.
         *
         * @return A string representing the result.
         */
        @Override
        public String toString() {
            return "%s=%sms".formatted(name, asMillis());
        }
    }
    
}
