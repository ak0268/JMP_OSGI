package com.jmp.osgi.bundle.component;

import com.jmp.osgi.bundle.iface.ILoggable;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.commons.scheduler.Job;
import org.apache.sling.commons.scheduler.JobContext;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import java.util.Date;

@Component(name="Settings Component", description="Component provides functionality of setting logging timeout and logging executor", metatype=true, immediate = true)
@Service(value = {SettingsComponent.class})
public class SettingsComponent implements Job {

    public static final String CONSOLE_IMPL = "CONSOLE";
    public static final String FILE_IMPL = "FILE";
    public static final String JOB_NAME = "SettingsComponent";

    @Property(label = "Logging executor list", name = "executor",
            options = {
                    @PropertyOption(name = "file", value = FILE_IMPL),
                    @PropertyOption(name = "console", value = CONSOLE_IMPL)
            }
    )
    private static final String EXECUTOR_NAME = "executor";

    @Property(label = "Scheduling period")
    private static final String LOGGING_PERIOD = "scheduler.period";

    @Reference
    private Scheduler scheduler;

    private ILoggable logger;

    @Activate
    private void activate(ComponentContext context) {
        updateLoggingState(context);
    }

    @Override
    public void execute(JobContext jobContext) {
        if (logger != null) {
            logger.printMessage(new Date().toString());
        }
    }

    @Modified
    private void updateLoggingState(ComponentContext context) {
        String schedulerPeriod = PropertiesUtil.toString(context.getProperties().get(LOGGING_PERIOD), "5");
        String period = "0/" + schedulerPeriod + " * * * * ?";
        String implementationConfigName = PropertiesUtil.toString(context.getProperties().get(EXECUTOR_NAME), CONSOLE_IMPL);

        BundleContext bundleContext = context.getBundleContext();
        try {
            ServiceReference[] serviceReferences = bundleContext.getServiceReferences(ILoggable.class.getName(), "(logging.target="+ implementationConfigName.toLowerCase() +")");
            if (serviceReferences != null && serviceReferences.length > 0) {
                ServiceReference serviceReference = serviceReferences[0];
                if (serviceReference != null) {
                    logger = (ILoggable) bundleContext.getService(serviceReference);
                }
            }
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        deactivate();

        try {
            scheduler.addJob(JOB_NAME, this, null, period, false);
        } catch (Exception e) {
            System.out.println("Job component activation error");
        }
    }

    @Deactivate
    private void deactivate() {
        scheduler.removeJob(JOB_NAME);
    }
}
