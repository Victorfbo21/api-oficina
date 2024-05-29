package arcee.oficinaback.users.records;

public record CreateFileResponse(String hash, String path, String userID, Number expire) {
}
