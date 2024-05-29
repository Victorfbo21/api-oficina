package arcee.oficinaback.configs;

public record AppResponse(Object data, Boolean error, Integer statusCode,String message) {
}
