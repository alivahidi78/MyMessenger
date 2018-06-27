package application.util.request;

public class UploadFileRequest extends Request {
    public final long fileSize;
    public final String fileName;

    public UploadFileRequest(String username, String password, long fileSize, String fileNameWithExtension) {
        super(username, password, RequestType.UPLOAD_FILE, true);
        this.fileSize = fileSize;
        this.fileName = fileNameWithExtension;
    }
}
