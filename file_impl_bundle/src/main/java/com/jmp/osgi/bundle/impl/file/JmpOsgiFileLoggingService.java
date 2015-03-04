package com.jmp.osgi.bundle.impl.file;

import com.jmp.osgi.bundle.iface.ILoggable;

import java.io.*;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

@Component(name = "JMP OSGi File Logging component", metatype = true, immediate = true)
@Service(value = ILoggable.class)
public class JmpOsgiFileLoggingService implements ILoggable {

    @Property(value = "file", propertyPrivate = true)
    private static final String FILE = "logging.target";

    private static final String FILE_NAME = "./jmp-osgi-log.txt";

    private File logFile;
    private PrintWriter writer;

    public JmpOsgiFileLoggingService() {
        this.logFile = new File(FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(this.logFile, true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    @Deactivate
    private void deactivate() {
        writer.close();
    }
}