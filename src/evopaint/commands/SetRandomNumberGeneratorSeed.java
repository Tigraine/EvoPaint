package evopaint.commands;

import evopaint.util.RandomNumberGeneratorWrapper;
import evopaint.interfaces.ICommand;

public class SetRandomNumberGeneratorSeed implements ICommand {
    private RandomNumberGeneratorWrapper rng;

    public SetRandomNumberGeneratorSeed(RandomNumberGeneratorWrapper rng) {
        this.rng = rng;
    }

    public void execute() {
        //TODO: Reset
    }
}
