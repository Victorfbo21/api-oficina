package arcee.oficinaback.users.records;

public record UpdatePasswordRecord(String code, String password, String confirmPassword, String email) {
}
