package org.finra.jtaf.core.mock;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;

public class MockCommand2 extends Command {
	public static boolean testEnd;
	
	public MockCommand2(String name) throws NameFormatException {
		super(name);
		testEnd = false;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
		// TODO Auto-generated method stub
		testEnd = true;
	}

}
