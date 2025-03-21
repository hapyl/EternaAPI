package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.SelfReturn;

import javax.annotation.Nonnull;

/**
 * Represents a callback style object that handles the given {@link Callback}.
 * <p>Implementation example:</p>
 *
 * <pre>{@code
 *
 * // Callback
 * class MyCallback implements Callback {
 *     void onHello();
 *     void onGoodbye();
 * }
 *
 * // Callback handler
 * class MyCallBackHandler implements CallbackHandler<MyCallback> {
 *     private MyCallback callback = new MyCallback() {...};
 *
 *     @SelfReturn
 *     public MyCallBackHandler callback(@Nonnull MyCallback callback) {
 *         this.callback = callback;
 *         return this;
 *     }
 *
 *     // Implement callback
 *     public void sayHello() {
 *         System.out.println("Hello!");
 *
 *         callback.onHello();
 *     }
 *
 *     public void sayHello() {
 *         System.out.println("Goodbye!");
 *
 *         callback.onGoodbye();
 *     }
 *
 * }
 *
 * }</pre>
 *
 * @param <C>
 */
public interface CallbackHandler<C extends Callback> {
    
    @SelfReturn
    CallbackHandler<C> callback(@Nonnull C callback);
    
}
