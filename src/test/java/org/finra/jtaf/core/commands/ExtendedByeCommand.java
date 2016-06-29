package org.finra.jtaf.core.commands;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;

public class ExtendedByeCommand extends ByeCommand{

	public ExtendedByeCommand(String name) throws NameFormatException {
		super(name);
	}

	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
	
		getContext().putObject("data", "Bye Bye " + getContext().getObject("data"));
	}

}
