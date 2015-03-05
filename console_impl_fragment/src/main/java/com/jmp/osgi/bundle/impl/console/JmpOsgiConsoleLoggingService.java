package com.jmp.osgi.bundle.impl.console;

import com.jmp.osgi.bundle.iface.ILoggable;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

@Component(name = "JMP OSGi Console Logging component",metatype = true, immediate = true)
@Property(value = "console", propertyPrivate = true, name = "logging.target")
@Service(value = ILoggable.class)
public class JmpOsgiConsoleLoggingService implements ILoggable {
    /* Logging executes in crx-quickstart/logs/stdout.log */
    public void printMessage(String message) {
        System.out.println(message);
    }
}
