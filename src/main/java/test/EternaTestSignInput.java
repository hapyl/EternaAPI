package test;

import me.hapyl.eterna.module.inventory.sign.SignInput;
import me.hapyl.eterna.module.inventory.sign.SignResponse;
import me.hapyl.eterna.module.inventory.sign.SignType;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Enums;
import org.jetbrains.annotations.NotNull;

public final class EternaTestSignInput extends EternaTest {
    
    EternaTestSignInput(@NotNull Key key) {
        super(key);
    }
    
    @Override
    public void test(@NotNull EternaTestContext context) throws EternaTestException {
        new SignInput(context.player(), Enums.getRandomValueOrFirst(SignType.class), "Is this a test?") {
            @Override
            public void onResponse(@NotNull SignResponse response) {
                final String firstLine = response.get(0).toString();
                final int secondLine = response.get(1).toInt();
                
                try {
                    context.assertEquals(firstLine, "Yes");
                    context.assertEquals(secondLine, 123456789);
                    
                    context.assertTestPassed();
                }
                catch (EternaTestException ex) {
                    context.assertTestFailed(ex.getMessage());
                }
            }
        };
    }
    
}