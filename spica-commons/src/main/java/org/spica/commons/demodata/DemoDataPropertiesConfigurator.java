package org.spica.commons.demodata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;

@Slf4j
public class DemoDataPropertiesConfigurator {


    public void configurations (File spicaFolder) throws IOException {
        File properties = new File (spicaFolder, "spica.properties");

        Properties demoProperties = new Properties();

        File globalProps = new File (SpicaProperties.getGlobalSpicaHome(), "spica.properties");
        Properties globalProperties = new Properties();
        globalProperties.load(new FileInputStream(globalProps));
        for (String nextName: globalProperties.stringPropertyNames()) {
            String nextValue = globalProperties.getProperty(nextName);
            demoProperties.setProperty(nextName, nextValue);
        }

        demoProperties.setProperty("spica.cli.serverurl", "http://localhost:8765/api");
        demoProperties.setProperty("spica.cli.username", "mm");
        demoProperties.setProperty("spica.cli.password", "mm");
        demoProperties.setProperty("spica.cli.usermail", "marc.mayer@spica.org");

        log.info("Configure properties in " + properties.getAbsolutePath());
        log.info("Properties: " + demoProperties);

        properties.getParentFile().mkdirs();
        demoProperties.store(new FileOutputStream(properties), "Created by " + getClass().getName());
    }

    public final static void main (final String [] args) throws IOException {
        File spicaFolder = new File (".spica");

        File configFile = new File(spicaFolder, "config.xml");
        if (configFile.exists())
            configFile.delete();

        DemoDataPropertiesConfigurator demoDataPropertiesConfigurator = new DemoDataPropertiesConfigurator();
        demoDataPropertiesConfigurator.configurations(spicaFolder);
    }
}
