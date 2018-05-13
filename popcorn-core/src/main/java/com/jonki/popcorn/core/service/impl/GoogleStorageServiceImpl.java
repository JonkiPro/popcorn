package com.jonki.popcorn.core.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.jonki.popcorn.common.dto.StorageDirectory;
import com.jonki.popcorn.common.exception.ResourcePreconditionException;
import com.jonki.popcorn.common.exception.ResourceServerException;
import com.jonki.popcorn.core.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the Storage Service for the Google cloud.
 */
@Service
@Qualifier("googleStorageService")
@Slf4j
@Validated
public class GoogleStorageServiceImpl implements StorageService {

    /** Application name. */
    private static final String APPLICATION_NAME =
            "My project";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws ResourceServerException Incorrect authorization
     */
    public static Credential authorize() {
        // Load client secrets.
        final InputStream in =
                GoogleStorageServiceImpl.class.getResourceAsStream("/client_secret.json");
        Credential credential;
        try {
            final GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            final GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline")
                            .build();
            credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");
        } catch (final IOException e) {
            throw new ResourceServerException("Incorrect authorization", e);
        }
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     */
    public static Drive getDriveService() {
        final Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String save(
            @NotNull final java.io.File file,
            @NotNull final StorageDirectory storageDirectory
    ) throws ResourcePreconditionException, ResourceServerException {
        // Build a new authorized API client service.
        final Drive service = getDriveService();
        final String folderId = storageDirectory.getGoogleDirectoryKey();

        File metadata;
        FileContent content;
        File execute;
        try {
            metadata = new File();
            metadata.setTitle(file.getCanonicalPath());
            metadata.setParents(Arrays.asList(new ParentReference().setId(folderId)));
            content = new FileContent(Files.probeContentType(file.toPath()), file);
        } catch (final IOException e) {
            throw new ResourcePreconditionException("An I/O error occurs");
        }

        try {
            execute = service.files().insert(metadata, content).execute();
            log.info("Saved file");
        } catch (final IOException e) {
            throw new ResourceServerException("The initialization of the request fails", e);
        }
        return execute.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(
            @NotBlank final String fileId
    ) throws ResourcePreconditionException, ResourceServerException {
        // Build a new authorized API client service.
        final Drive service = getDriveService();

        try {
            service.files().delete(fileId).execute();
            log.info("Deleted file");
        } catch (final IOException e) {
            throw new ResourceServerException("The initialization of the request fails", e);
        }
    }
}
