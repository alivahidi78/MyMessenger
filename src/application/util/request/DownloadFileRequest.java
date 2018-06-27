package application.util.request;

public class DownloadFileRequest extends Request {
    public DownloadFileRequest(String username, String password) {
        super(username, password, RequestType.DOWNLOAD_FILE, true);
    }
}
