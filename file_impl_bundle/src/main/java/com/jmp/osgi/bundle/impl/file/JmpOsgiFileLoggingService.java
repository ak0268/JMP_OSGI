package com.jmp.osgi.bundle.impl.file;

import com.jmp.osgi.bundle.iface.ILoggable;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(name = "JMP OSGi File Logging component", metatype = true, immediate = true)
@Property(value = "file", propertyPrivate = true, name = "logging.target")
@Service(value = ILoggable.class)
public class JmpOsgiFileLoggingService implements ILoggable {

    private Logger logger = LoggerFactory.getLogger(JmpOsgiFileLoggingService.class);

    public void printMessage(String message) {
        logger.info(message);
    }
}