package org.cherno.twapp2.restserv.sdargs;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

/**
* Created by Solovjev Ivan on 07.11.2014.
*/
public class FileArgValidator implements IParameterValidator {
    public void validate(String name, String value) throws ParameterException {
        if (value.isEmpty()) {
            throw new ParameterException("Parameter " + name + " can't be empty");
        }
        if(!(new File(value)).exists()) {
            System.out.println("File " + value + " doesn't exist");
            throw new ParameterException("File " + value + " doesn't exist");
        }
    }
}
