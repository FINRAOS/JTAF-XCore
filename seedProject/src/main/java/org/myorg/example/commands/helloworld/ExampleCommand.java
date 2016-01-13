package org.myorg.example.commands.helloworld;

import java.util.Map;

import org.finra.jtaf.core.model.exceptions.NameFormatException;
import org.finra.jtaf.core.model.execution.IInvocationContext;
import org.finra.jtaf.core.model.invocationtarget.Command;
import org.junit.Assert;

public class ExampleCommand extends Command {

  public ExampleCommand(String name) throws NameFormatException {
    super(name);
  }

  protected void execute(IInvocationContext ctx) throws Throwable {
    String expectedText="Hello world";
    String actualText=getRequiredString("assertableText");
    
    Assert.assertEquals("Your text doesn't match " + expectedText, expectedText, actualText);
    
    @SuppressWarnings("unchecked")
	Map<String, Object> map = (Map<String, Object>) getOptionalObject("mapToPrint");
    
    if(map != null) {
      for(String k : map.keySet()) {
        System.out.println(k + ": " + map.get(k));
      }
    }
    
    boolean printHello = getBooleanOrDefault("printHello", false);
    if(printHello) {
      System.out.println("Hello.");
    }
  }
}