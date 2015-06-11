package org.cherno.twapp2.restserv.sdargs;

import com.beust.jcommander.IStringConverter;

import java.io.File;

/**
 * Created by Solovjev Ivan on 07.11.2014.
 */
public class FileArgConverter implements IStringConverter<File> {
    @Override
    public File convert(String value) {
        return new File(value);
    }
}
