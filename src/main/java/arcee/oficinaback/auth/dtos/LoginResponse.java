package arcee.oficinaback.auth.dtos;

import arcee.oficinaback.auth.LoginUserResponse;

public record LoginResponse(String accessToken, Long expiresIn, LoginUserResponse user ) {
}
