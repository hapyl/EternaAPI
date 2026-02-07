package test;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class EternaTestException extends RuntimeException {

    private final EternaTest test;

    EternaTestException(@NotNull EternaTest test, @NotNull String message) {
        super(message);

        this.test = test;
    }
    
    @Override
    public String toString() {
        return getMessage();
    }
}
