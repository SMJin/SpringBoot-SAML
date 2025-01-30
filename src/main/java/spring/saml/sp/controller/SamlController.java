package spring.saml.sp.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import spring.saml.sp.service.SamlApiService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/saml")
@RequiredArgsConstructor
public class SamlController {

    private final SamlApiService samlApiService;

    @GetMapping("/login")
    public RedirectView redirectToIdP() {
        String samlRequest = samlApiService.createSamlRequest();
        String redirectUrl = samlApiService.getRedirectUrl(samlRequest);
        return new RedirectView(redirectUrl);
    }
    @GetMapping("/user")
    public Map<String, String> getUserInfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("email", email);
        return userInfo;
    }
}
