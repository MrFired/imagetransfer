package personal.mrfired.imagetransfer.service.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import personal.mrfired.imagetransfer.service.storage.exceptions.StorageException;
import personal.mrfired.imagetransfer.service.storage.exceptions.StorageFileNotFoundException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class CloudStorageService implements StorageService {
    private final S3Client s3Client;
    private final String bucketName;

    @Autowired
    public CloudStorageService(@Value("${yacloud.bucket-name}") String bucketName,
                               @Value("${yacloud.endpoint}") String endpoint,
                               @Value("${yacloud.region}") String region,
                               @Value("${AWS_ACCESS_KEY_ID_FILE}") String awsAccessKeyIdFile,
                               @Value("${AWS_SECRET_ACCESS_KEY_FILE}") String awsSecretAccessKeyFile) throws IOException {
        this.bucketName = bucketName;

        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        Files.readString(Path.of(awsAccessKeyIdFile)),
                                        Files.readString(Path.of(awsSecretAccessKeyFile))
                                )
                        )
                )
                .endpointOverride(URI.create(endpoint))
                .build();
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty())
            throw new StorageException("Failed to store empty file.");

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty())
            throw new StorageException("Cannot store file without name.");

        filename = UUID.randomUUID() + "-" + filename;

        try {
            byte[] body = file.getBytes();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(body));
        } catch (IOException | S3Exception e) {
            throw new StorageException("Failed to store file.", e);
        }

        return filename;
    }

    @Override
    public Resource loadAsResource(String filename) {
        if (filename == null || filename.isEmpty())
            throw new StorageException("Cannot store file without name.");

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            return new ByteArrayResource(s3Client.getObjectAsBytes(request).asByteArray());
        } catch (NoSuchKeyException e) {
            throw new StorageFileNotFoundException("File with name '" + filename + "' is not found", e);
        } catch (S3Exception e) {
            throw new StorageException("Failed to load file.", e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            if (filename == null || filename.isEmpty())
                throw new StorageException("Cannot delete file without name.");

            if (!fileExists(filename))
                throw new StorageFileNotFoundException("File with name '" + filename + "' was not found.");

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            s3Client.deleteObject(request);
        } catch (S3Exception e) {
            throw new StorageException("Failed to delete file.", e);
        }
    }

    private boolean fileExists(String filename) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            HeadObjectResponse response = s3Client.headObject(request);
            return response.sdkHttpResponse().statusCode() != HttpStatusCode.NOT_FOUND;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }
}
