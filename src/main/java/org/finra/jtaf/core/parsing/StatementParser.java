/*
 * (C) Copyright 2014 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.finra.jtaf.core.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.finra.jtaf.core.model.statement.Invocation;
import org.finra.jtaf.core.model.statement.InvocationList;
import org.finra.jtaf.core.parsing.exceptions.ExceptionAccumulator;
import org.finra.jtaf.core.parsing.exceptions.ParsingException;
import org.finra.jtaf.core.parsing.exceptions.UnexpectedElementException;
import org.finra.jtaf.core.parsing.helpers.AttributeHelper;
import org.finra.jtaf.core.parsing.helpers.ElementScanner;
import org.finra.jtaf.core.parsing.helpers.ParserHelper;
import org.finra.jtaf.core.utilities.logging.MessageCollector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Responsible for parsing statement in test script.
 */
public class StatementParser {
	
	private static final List<String> NON_BLOCK_PARAMETER_TYPES = Collections.unmodifiableList(Arrays.asList("param", "list", "string", "map", "boolean"));
	private static final List<String> STRING_PARAMETER_TYPES = Collections.unmodifiableList(Arrays.asList("param", "string"));

	public final InvocationList processStatementList(Element elem, MessageCollector mc)
          throws ParsingException {
      // Create formal try...recover...cleanup blocks
      preprocessStatementList(elem, mc);

      InvocationList retval = new InvocationList();
      for (Element child : ParserHelper.getChildren(elem)) {

          retval.add(processStatement(child, mc));
      }
      return retval;
  }

  //TODO: This needs to come across a TryRecoverCleanup and handle that
  private final void preprocessStatementList(Element elem, MessageCollector mc)
          throws ParsingException {

      ElementScanner es = new ElementScanner(ParserHelper.getChildren(elem));
      Element next = null;
      // Element tRC =
      // elem.getOwnerDocument().createElement("TryRecoverCleanup");
      if (!elem.getNodeName().equals("TryRecoverCleanup")) {
          while (es.hasNext()) {

              if ((next = es.tryMatch("try")) != null) {
                  Element safety = elem.getOwnerDocument().createElement("TryRecoverCleanup");
                  Element tryBlock = elem.getOwnerDocument().createElement("try");
                  NodeList childNodes = next.getChildNodes();
                  for (int i = 0; i < childNodes.getLength(); i++) {
                      tryBlock.appendChild(childNodes.item(i).cloneNode(true));

                  }
                  elem.replaceChild(safety, next);
                  safety.appendChild(tryBlock);

                  if ((next = es.tryMatch("recover")) != null) {
                      elem.removeChild(next);
                      Element recoverBlock = elem.getOwnerDocument().createElement("recover");
                      childNodes = next.getChildNodes();
                      for (int i = 0; i < childNodes.getLength(); i++) {
                          recoverBlock.appendChild(childNodes.item(i).cloneNode(true));

                      }
                      safety.appendChild(recoverBlock);
                  }

                  if ((next = es.tryMatch("cleanup")) != null) {
                      elem.removeChild(next);
                      Element cleanUpBlock = elem.getOwnerDocument().createElement("cleanup");
                      childNodes = next.getChildNodes();
                      for (int i = 0; i < childNodes.getLength(); i++) {
                          cleanUpBlock.appendChild(childNodes.item(i).cloneNode(true));

                      }
                      safety.appendChild(cleanUpBlock);
                  }

                  continue;
              } else if (((next = es.tryMatch("recover")) != null)
                      || ((next = es.tryMatch("cleanup")) != null)) {
                  throw new UnexpectedElementException(next);
              }

              // Move on to the next element
              es.match();
          }
      }
  }

  private final Invocation processStatement(Element elem, MessageCollector mc)
          throws ParsingException {
      try {
          mc.push(elem.getNodeName());

          return processInvocation(elem, mc);

      } catch (ParsingException e) {
          mc.error(e.getMessage());
          throw e;
      } finally {
          mc.pop();
      }
  }



 
  // TODO Use this in order to put the child nodes of the command as the block
  // Calls processMap which calls process object. This method then deals with
  // strings, maps, and lists
  // This can be changed so that it either does processCommand or processX
  // depending no what
  // the default value is
  private final Invocation processInvocation(Element elem, MessageCollector mc)
          throws ParsingException {
      Invocation retval = new Invocation(elem.getNodeName());
      Map<String, List<Object>> invocationAttributesMap = new HashMap<String, List<Object>>();

      // get properties from children
      // TODO: This will process all children of the command
      preprocessStatementList(elem, mc);
      invocationAttributesMap.putAll(processInvocationChildNodes(elem, mc));

      // get properties from attributes
      final AttributeHelper attrs = new AttributeHelper(elem);

      // invocationAttributesMap.putAll(temp);
      retval.setParameters(invocationAttributesMap);
      retval.getParameters().putAll(attrs.getMap());
      retval.getParameters().putAll(processMap(elem, mc));

      return retval;
  }

  // TODO: This needs to send a child node to processInvocation if it is a
  // command
  private final Map<String, List<Object>> processInvocationChildNodes(Element elem,
          MessageCollector mc) throws ParsingException {
      boolean blockCreated = false;
      try {
          mc.push(elem.getNodeName());
          Map<String, List<Object>> result = new HashMap<String, List<Object>>();
          List<Object> block = new ArrayList<Object>();
          NodeList childNodes = elem.getChildNodes();
          // there used to be a check for null here, but the JavaDoc makes no
          // mention that this is possible and I can find no examples online
          // of how to  cause that
          if (childNodes.getLength() > 0) {
              for (int i = 0; i < childNodes.getLength(); i++) {
                  Node currentNode = childNodes.item(i);
                  if (currentNode.getNodeType() == Node.ELEMENT_NODE
                		  && !isNonBlockParameterType(currentNode.getNodeName())) {
                      blockCreated = true;
                      Invocation child = processInvocation((Element) currentNode, mc);
                      block.add(child);

                      /**
                       * else { Object thing = processObject((Element)
                       * currentNode, mc); block.add(thing); }
                       **/

                      // TODO: processInvocation to get a command handled.
                      // It should then be stored in result
                      // processInvocation(currentNode, mc);
                      // String nodeName = currentNode.getNodeName();
                  }

              }
              if (blockCreated) {

                  result.put("blockParam", block);
              }
          }
          return result;
      }
      finally {
          mc.pop();
      }
  }


  private boolean isNonBlockParameterType(String nodeName) {
	  return NON_BLOCK_PARAMETER_TYPES.contains(nodeName.toLowerCase());
  }

private final Map<String, Object> processMap(Element elem, MessageCollector mc)
          throws ParsingException {
      ExceptionAccumulator acc = new ExceptionAccumulator();
      HashMap<String, Object> retval = new HashMap<String, Object>();

      for (Element child : ParserHelper.getChildren(elem)) {
          try {
              final AttributeHelper attrs = new AttributeHelper(child);
              Object value = processObject(child, mc);
              if (value != null) {
                  String key = attrs.getRequiredString("name");
                  retval.put(key, value);
              }
          } catch (Throwable th) {
              mc.error(th.getMessage());
              acc.add(th);
          }
      }

      if (!acc.isEmpty()) {
          throw acc;
      }

      return retval;
  }

  private final List<Object> processList(Element elem, MessageCollector mc)
          throws ParsingException {

      try {
          mc.push(elem.getNodeName());
          ArrayList<Object> retval = new ArrayList<Object>();
          for (Element child : ParserHelper.getChildren(elem)) {
              retval.add(processObject(child, mc));
          }
          return retval;
      } finally {
          mc.pop();
      }

  }

  private final Object processObject(Element elem, MessageCollector mc) throws ParsingException {
      try {

          mc.push("In data element " + elem.getNodeName());
          final String name = elem.getNodeName().toLowerCase();
//          if (name.equals("param") || name.equals("string")) {
          if (isStringParameterType(name)) {
              return processString(elem, mc);
          } else if (name.equals("map")) {
              return processMap(elem, mc);
          } else if (name.equals("list")) {
              return processList(elem, mc);
          } else {
              return null;
          }
      } finally {
          mc.pop();
      }
  }
  
  private boolean isStringParameterType(String parameterType) {
	  return STRING_PARAMETER_TYPES.contains(parameterType.toLowerCase());
  }
  
  // something strange is going on here. why can <string>'s have child nodes? why is this public?
  public Object processString(Element elem, MessageCollector mc) throws ParsingException {
      try {
          mc.push(elem.getNodeName());
          Map<String, List<Object>> childNodeValue = processInvocationChildNodes(elem, mc);
          AttributeHelper ah = new AttributeHelper(elem);
          if (ah != null && ah.entrySet().size() > 0) {
              if (ah.containsKey("name") && (ah.entrySet().size() == 1)) {
                  // this is map...
              } else {
                  return ah;
              }
          } else if (!childNodeValue.isEmpty()) {
              for (String key : childNodeValue.keySet()) {
                  return childNodeValue.get(key);
              }
          }

          return elem.getTextContent();
      } finally {
          mc.pop();
      }
  }


}
