package ba.ramiz.synonyms.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    @Test
    void authenticate_returnsOAuthResponse() {
        // arrange
        String username = "user1";
        String password = "pass1";
        String scope = "reader";
        OAuthResponse expectedResponse = new OAuthResponse("token1", "Bearer", 3600);

        when(tokenService.getToken(username, password, scope)).thenReturn(expectedResponse);

        // act
        OAuthResponse actualResponse = authController.authenticate(username, password, scope);

        // assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getAccessToken(), actualResponse.getAccessToken());
        assertEquals(expectedResponse.getTokenType(), actualResponse.getTokenType());
        assertEquals(expectedResponse.getExpiresIn(), actualResponse.getExpiresIn());
    }

}

