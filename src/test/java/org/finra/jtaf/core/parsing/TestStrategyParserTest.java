package org.finra.jtaf.core.parsing;

import java.io.File;
import java.util.ArrayList;

import org.finra.jtaf.core.parsing.exceptions.UnexpectedElementException;
import org.finra.jtaf.core.plugins.parsing.IPostParseStrategyElementPlugin;
import org.junit.Before;
import org.junit.Test;

public class TestStrategyParserTest {
    private static final String INVALID_ROOT_STRATEGY_FILE_NAME = "parser_testing/InvalidRoot.strategy.xml";
    private static final String MISSING_TARGET_NAME_STRATEGY_FILE_NAME = "parser_testing/MissingTargetNameAttribute.strategy.xml";

    private TestStrategyParser testStrategyParser;

    @Before
    public void before() throws Exception {
        testStrategyParser = new TestStrategyParser();
        testStrategyParser.setPostParseStrategyElementPlugins(new ArrayList<IPostParseStrategyElementPlugin>());
    }

    @Test(expected = UnexpectedElementException.class)
    public void testParseInvalidRoot() throws Exception {
        testStrategyParser.parse(new File(INVALID_ROOT_STRATEGY_FILE_NAME));
    }

    @Test
    public void testParseMissingTargetName() throws Exception {
        testStrategyParser.parse(new File(MISSING_TARGET_NAME_STRATEGY_FILE_NAME));
    }
}
