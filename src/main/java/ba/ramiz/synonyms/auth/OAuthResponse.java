package ba.ramiz.synonyms.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OAuthResponse {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("scope")
    private String scope;


    public OAuthResponse(String token1, String bearer, int i) {
    }
}
