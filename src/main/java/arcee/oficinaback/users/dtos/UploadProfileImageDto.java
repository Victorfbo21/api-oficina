package arcee.oficinaback.users.dtos;

import org.springframework.http.MediaType;

import java.nio.Buffer;

public class UploadProfileImageDto {


    private byte[] data;

    private String filename;

    private MediaType fileType;

    private long fileSize;

    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public MediaType getFileType() {
        return fileType;
    }

    public void setFileType(MediaType fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
