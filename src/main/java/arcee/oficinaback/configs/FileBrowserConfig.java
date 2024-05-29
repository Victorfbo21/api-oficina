package arcee.oficinaback.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "filebrowser")
public class FileBrowserConfig {


    private String loginUrl;
    private String baseUploadPath;
    private String shareUploadPublic;
    private String sharedUrl;
    private String username;
    private String password;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getBaseUploadPath() {
        return baseUploadPath;
    }

    public void setBaseUploadPath(String baseUploadPath) {
        this.baseUploadPath = baseUploadPath;
    }

    public String getShareUploadPublic() {
        return shareUploadPublic;
    }

    public void setShareUploadPublic(String shareUploadPublic) {
        this.shareUploadPublic = shareUploadPublic;
    }

    public String getSharedUrl() {
        return sharedUrl;
    }

    public void setSharedUrl(String sharedUrl) {
        this.sharedUrl = sharedUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
