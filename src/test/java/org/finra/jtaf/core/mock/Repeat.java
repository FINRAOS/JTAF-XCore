package org.finra.jtaf.core.mock;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;

public class Repeat extends Command{

	public Repeat(String name) throws NameFormatException {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
		// TODO Auto-generated method stub
		//do nothing
	}

}
