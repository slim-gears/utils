/**
 *
 */
package com.slimgears.util.guice;

import java.util.Properties;

public interface ConfigProvider {
    void apply(Properties properties);
}
