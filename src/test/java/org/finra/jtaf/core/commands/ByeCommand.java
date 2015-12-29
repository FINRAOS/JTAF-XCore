package org.finra.jtaf.core.commands;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;

public class ByeCommand extends Command {

    public ByeCommand(String name) throws NameFormatException {
        super(name);
    }

    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {

        getContext().putObject("data", "Bye !");
    }

}
