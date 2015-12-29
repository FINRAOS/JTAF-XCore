package org.finra.jtaf.core.mock;

import java.sql.SQLException;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;

public class IThrowExceptionCmd extends Command {

    public IThrowExceptionCmd(String name) throws NameFormatException {
        super(name);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void execute(IInvocationContext ctx) throws Throwable {
        throw new SQLException("This is just a test");

    }

}
