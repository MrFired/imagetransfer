package personal.mrfired.imagetransfer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Entity
@Table(name = "images")
public class Image {
    private static final SimpleDateFormat format;

    static {
        format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
    }

    @Id
    private String name;
    private String userUploaded;
    private Timestamp uploadTimestamp;

    public Image(@NonNull String name, String userUploaded, Timestamp uploadTimestamp) {
        this.name = name;
        this.userUploaded = userUploaded;
        this.uploadTimestamp = uploadTimestamp;
    }

    public Image() {}

    public String getName() {
        return name;
    }

    public String getUserUploaded() {
        return userUploaded;
    }

    public Timestamp getUploadTimestamp() {
        return uploadTimestamp;
    }

    public String getFormattedUploadTimestamp() {
        return format.format(uploadTimestamp);
    }
}
