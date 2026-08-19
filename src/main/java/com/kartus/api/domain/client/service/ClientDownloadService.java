package com.kartus.api.domain.client.service;

import com.kartus.api.domain.client.dto.ClientManifestDTO;
import com.kartus.api.domain.client.error.ClientErrorCode;
import com.kartus.api.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.exc.JacksonIOException;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;

@Slf4j
@Service
public class ClientDownloadService {
    private static final String MANIFEST_FILE_NAME = "manifest.json";

    private final ObjectMapper objectMapper;
    private final Path basePath;

    public ClientDownloadService(
            ObjectMapper objectMapper,
            @Value("${client.download.base-path}") String basePath
    ) {
        if (!StringUtils.hasText(basePath)) {
            throw new IllegalStateException(
                    "client.download.base-path must not be blank. Set CLIENT_DOWNLOAD_BASE_PATH in .env");
        }

        this.objectMapper = objectMapper;
        this.basePath = Path.of(basePath).toAbsolutePath().normalize();
    }

    public ClientDownload resolve(String platform) {
        if (!StringUtils.hasText(platform)) {
            throw new CustomException(ClientErrorCode.PLATFORM_NOT_AVAILABLE);
        }

        ClientManifestDTO manifest = readManifest();

        String version = requireText(manifest.version());

        if (manifest.platforms() == null) {
            throw new CustomException(ClientErrorCode.MANIFEST_INVALID);
        }

        ClientManifestDTO.PlatformDTO entry = manifest.platforms().get(platform);
        if (entry == null) {
            throw new CustomException(ClientErrorCode.PLATFORM_NOT_AVAILABLE);
        }

        String fileName = requireText(entry.fileName());
        Path file = resolveSafely(version, fileName);

        FileSystemResource resource = new FileSystemResource(file);
        if (!resource.exists() || !resource.isReadable()) {
            log.error("client binary missing or unreadable: {}", file);
            throw new CustomException(ClientErrorCode.CLIENT_FILE_NOT_FOUND);
        }

        return new ClientDownload(resource, fileName);
    }

    private ClientManifestDTO readManifest() {
        try {
            return objectMapper.readValue(basePath.resolve(MANIFEST_FILE_NAME), ClientManifestDTO.class);
        } catch (JacksonException e) {
            log.error("failed to read client manifest under {}", basePath, e);
            throw new CustomException(e instanceof JacksonIOException
                    ? ClientErrorCode.MANIFEST_NOT_READABLE
                    : ClientErrorCode.MANIFEST_INVALID);
        }
    }

    private String requireText(String value) {
        if (!StringUtils.hasText(value)) {
            throw new CustomException(ClientErrorCode.MANIFEST_INVALID);
        }

        return value;
    }

    private Path resolveSafely(String version, String fileName) {
        if (fileName.contains("/") || fileName.contains("\\")) {
            log.error("rejected file name containing a path separator: {}", fileName);
            throw new CustomException(ClientErrorCode.MANIFEST_INVALID);
        }

        Path resolved = basePath.resolve(version).resolve(fileName).normalize();

        if (!resolved.startsWith(basePath) || resolved.equals(basePath)) {
            log.error("rejected path escaping base path: version={}, fileName={}", version, fileName);
            throw new CustomException(ClientErrorCode.MANIFEST_INVALID);
        }

        return resolved;
    }

    public record ClientDownload(Resource resource, String fileName) {
    }
}
