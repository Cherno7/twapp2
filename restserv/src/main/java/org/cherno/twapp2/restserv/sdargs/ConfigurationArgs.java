package org.cherno.twapp2.restserv.sdargs;

import com.beust.jcommander.Parameter;

import java.io.File;

/**
 * Created by Solovjev Ivan on 07.11.2014.
 */
public class ConfigurationArgs {
    @Parameter(names = {"-c", "--config"}, description = "File with project configuration", required = false, converter = FileArgConverter.class, validateWith = FileArgValidator.class)
    private File configurationFile;

    public File getConfigurationFile() {
        return configurationFile;
    }
}
