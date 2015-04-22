package com.jmp.osgi.bundle.dropbox.job.consumer;

import com.day.cq.dam.api.AssetManager;
import com.jmp.osgi.bundle.dropbox.constants.DropboxJobConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Component(immediate = true, enabled = true)
@Service(JobConsumer.class)
@Property(name = JobConsumer.PROPERTY_TOPICS, value = DropboxJobConstants.DROPBOX_JOB_TOPIC)
public class DropBoxJobConsumer implements JobConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropBoxJobConsumer.class);

    public static final String DROPBOX_DAM_ROOTFOLDER = "/content/dam/";
    public static final String DEFAULT_MIME_TYPE = "image/jpeg";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public JobResult process(Job job) {
        JobResult result = JobResult.FAILED;
        try {
            String[] files = (String[]) job.getProperty(DropboxJobConstants.DROPBOX_RESOURCE_URL_PARAM);
            String targetFolder = (String) job.getProperty(DropboxJobConstants.DROPBOX_TARGET_FOLDER_PARAMETER);

            for (String file : files) {
                URL fileUrl = new URL(file);
                String fileName = FilenameUtils.getBaseName(String.valueOf(fileUrl));
                String targetRepositoryPath = DROPBOX_DAM_ROOTFOLDER + targetFolder + fileName;

                InputStream resourceInputStream = fileUrl.openStream();
                String mimeType = URLConnection.guessContentTypeFromStream(resourceInputStream);

                ResourceResolver resourceResolver = null;
                resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);

                AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
                assetManager.createAsset(targetRepositoryPath, resourceInputStream, (mimeType != null) ? mimeType : DEFAULT_MIME_TYPE, true);
            }
        } catch (MalformedURLException e) {
            LOGGER.error("Invalid Dropbox url", e);
        } catch (IOException e) {
            LOGGER.error("Input stream extracting error", e);
        } catch (LoginException e) {
            LOGGER.error("Repository authentication error", e);
        }
        result = JobResult.OK;

        return result;
    }
}