package com.jmp.osgi.bundle.dropbox.servlet;

import com.jmp.osgi.bundle.dropbox.constants.DropboxJobConstants;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

@SlingServlet(paths = "/bin/repository/storeDropboxResource", methods = "GET", metatype = true)
public class DropboxSynchronizationServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxSynchronizationServlet.class);

    @Reference
    private JobManager jobManager;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String[] fileUrls = request.getParameterValues(DropboxJobConstants.DROPBOX_FILES_LIST_PARAMETER);

        HashMap<String, Object> jobProps = new HashMap<String, Object>();
        jobProps.put(DropboxJobConstants.DROPBOX_RESOURCE_URL_PARAM, fileUrls);
        jobProps.put(DropboxJobConstants.DROPBOX_TARGET_FOLDER_PARAMETER, request.getParameter(DropboxJobConstants.DROPBOX_TARGET_FOLDER_PARAMETER));

        Job job = jobManager.addJob(DropboxJobConstants.DROPBOX_JOB_TOPIC, null, jobProps);
        LOGGER.info("Dropbox Job State: ", job.getJobState());
    }
}