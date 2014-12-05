package org.finra.jtaf.core.mock;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;

public class MockCommand1 extends Command{

	public MockCommand1(String name) throws NameFormatException {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
	
		getRequiredBoolean("param1");
	
	}

}
