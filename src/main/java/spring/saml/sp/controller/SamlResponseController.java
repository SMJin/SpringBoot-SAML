package spring.saml.sp.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.util.Base64;

@RestController
@RequestMapping("/saml")
public class SamlResponseController {

    @PostMapping("/consume")
    public RedirectView
//    public String
    consumeSamlResponse(@RequestParam("SAMLResponse") String samlResponse, HttpSession session) throws Exception {
        byte[] decodedResponse = Base64.getDecoder().decode(samlResponse);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new java.io.ByteArrayInputStream(decodedResponse));

        NodeList nodeList = document.getElementsByTagName("saml:NameID");
        if (nodeList.getLength() > 0) {
            String username = nodeList.item(0).getTextContent();
            session.setAttribute("username", username);
            session.setAttribute("email", username + "@example.com");

            // 🔹 인증이 완료되면 React 페이지로 리디렉트
            return new RedirectView("http://localhost:3000");
//            return "User authenticated: " + username;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SAML Response invalid");
        }
    }
}
