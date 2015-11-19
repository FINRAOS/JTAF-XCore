package org.finra.jtaf.core.commands;

import java.util.ArrayList;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.finra.jtaf.core.model.statement.Invocation;

/**
 * This command surrounds a block of commands to be repeatedly executed based on the number of elements in the
 * list parameter. In each iteration the current element from the list is placed in the context variable with the
 * name provided in the storeElementFromListAs parameter so that the same block of test can be executed with different parameters each time.
 * @author k25183
 *
 */
public class IterateOverList extends Command {

	/**
	 * The recordResult variable is set to false because details
     * about the step aren't needed, unlike the commands within the block
     *
	 * @param name of the command
	 * @throws NameFormatException
	 */
	public IterateOverList(String name) throws NameFormatException {
		super(name);
		recordResult=false;
	}

	/**
	 * This command repeatedly executes the surrounding block of commands based on
	 * the number of elements in the list parameter.
	 * In each iteration the current element from the list is placed in the context variable with the
	 * name provided in the storeElementFromListAs parameter so that the same block of test can be executed with different parameters each time.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void execute(IInvocationContext ctx) throws Throwable {
		ArrayList<Object> params = (ArrayList<Object>) this.getOptionalObject("blockParam");
        String listName = getRequiredString("list");
        ArrayList<Object> list = (ArrayList<Object>)getOptionalObject(listName);
        String saveElementAs = getRequiredString("storeElementFromListAs");
        if (params != null) {
        	for (Object element : list) {
        		ctx.putObject(saveElementAs,element);
                for (Object child : params) {
                    executeInvocation((Invocation) child);
                }
                ctx.removeObject(saveElementAs);
            }
        }
    }

}