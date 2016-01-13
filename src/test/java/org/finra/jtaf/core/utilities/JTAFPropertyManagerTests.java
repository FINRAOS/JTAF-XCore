package org.finra.jtaf.core.utilities;

import org.junit.Assert;
import org.junit.Test;

public class JTAFPropertyManagerTests {
    private static final String PRESET_SYSTEM_PROPERTY = "test.property.system";
    private static final String PRESET_SYSTEM_PROPERTY_VALUE = "preset system property value";

    private static final String FILE_PROPERTY = "test.property.file";
    private static final String FILE_PROPERTY_VALUE = "file property value"; // make sure to set in jtaf.properties also

    private static final String BOTH_PROPERTY = "test.property.both";
    private static final String BOTH_PROPERTY_VALUE_SYSTEM = "set in both (system)";
    private static final String BOTH_PROPERTY_VALUE_FILE = "set in both (file)";

    @Test
    public void testPresetSystemProperty() {
        System.setProperty(JTAFPropertyManager.PROPERTY_PREFIX + "." + PRESET_SYSTEM_PROPERTY, PRESET_SYSTEM_PROPERTY_VALUE);
        String fetchedValue = JTAFPropertyManager.getInstance().getProperty(PRESET_SYSTEM_PROPERTY);
        Assert.assertEquals(PRESET_SYSTEM_PROPERTY_VALUE, fetchedValue);
    }

    @Test
    public void testFileProperty() {
        String fetchedValue = JTAFPropertyManager.getInstance().getProperty(FILE_PROPERTY);
        Assert.assertEquals(FILE_PROPERTY_VALUE, fetchedValue);
    }

    @Test
    public void testPresetSystemPropertyAndFileProperty() {
        System.setProperty(JTAFPropertyManager.PROPERTY_PREFIX + "." + BOTH_PROPERTY, BOTH_PROPERTY_VALUE_SYSTEM);
        String fetchedValue = JTAFPropertyManager.getInstance().getProperty(BOTH_PROPERTY);
        Assert.assertFalse(BOTH_PROPERTY_VALUE_FILE.equals(fetchedValue));
        Assert.assertEquals(BOTH_PROPERTY_VALUE_SYSTEM, fetchedValue);
    }
}
