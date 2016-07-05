package org.finra.jtaf.core.commands;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;

public class ExtendedHelloCommand extends HelloCommand {

	public ExtendedHelloCommand(String name) throws NameFormatException {
		super(name);
	}

	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
	
		super.execute(ctx);
		String data = (String) getContext().getObject("data");
		data = "Hello Hello" + data;
	}

}
