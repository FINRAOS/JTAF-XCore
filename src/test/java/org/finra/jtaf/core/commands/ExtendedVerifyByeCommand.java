package org.finra.jtaf.core.commands;


import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.junit.Assert;

public class ExtendedVerifyByeCommand extends Command {

	public ExtendedVerifyByeCommand(String name) throws NameFormatException {
		super(name);
	}

	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
		String data = getStringOrDefault("data", "");
		Assert.assertEquals(data, "Bye Bye Bye !");
	}

}
