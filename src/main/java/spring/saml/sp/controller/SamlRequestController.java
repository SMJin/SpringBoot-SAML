package spring.saml.sp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import spring.saml.sp.service.SamlApiService;

@RestController
@RequestMapping("/saml")
@RequiredArgsConstructor
public class SamlRequestController {

    private final SamlApiService samlApiService;

    @GetMapping("/login")
    public RedirectView redirectToIdP() {
        String samlRequest = samlApiService.createSamlRequest();
        String redirectUrl = samlApiService.getRedirectUrl(samlRequest);
        return new RedirectView(redirectUrl);
    }
}
