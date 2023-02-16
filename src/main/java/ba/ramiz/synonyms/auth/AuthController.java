package ba.ramiz.synonyms.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/authenticate")
    public OAuthResponse authenticate(@RequestParam String username, @RequestParam String password, @RequestParam String scope) {
        OAuthResponse oAuthResponse = tokenService.getToken(username, password, scope);
        return oAuthResponse;
    }

}
