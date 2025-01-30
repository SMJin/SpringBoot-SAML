package spring.saml.sp.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class SamlApiService {

    public String getRedirectUrl(String samlRequest) {
        String redirectUrl = "http://localhost:8081/realms/identity-provider/protocol/saml/clients/spring-saml-sp"
                + "?SAMLRequest=" + URLEncoder.encode(samlRequest, StandardCharsets.UTF_8)
                + "&RelayState=" + URLEncoder.encode("/dashboard", StandardCharsets.UTF_8);

        System.out.println("Redirect to: " + redirectUrl);
        return redirectUrl;
    }

    public String createSamlRequest() {
        try {
            Document authnRequestDoc = getAuthnRequest();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(authnRequestDoc);
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));

            return Base64.getEncoder().encodeToString(writer.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SAML request - AuthnRequest", e);
        }
    }

    private Document getAuthnRequest() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element authnRequest = document.createElementNS("urn:oasis:names:tc:SAML:2.0:protocol", "samlp:AuthnRequest");
            authnRequest.setAttribute("AssertionConsumerServiceURL", "http://localhost:8080/saml/consume");
            authnRequest.setAttribute("ID", "_" + UUID.randomUUID());
            authnRequest.setAttribute("IssueInstant", Instant.now().toString());
            authnRequest.setAttribute("ProtocolBinding", "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
            authnRequest.setAttribute("ProviderName", "spring-saml-sp");
            authnRequest.setAttribute("Version", "2.0");

            Element issuerElement = document.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Issuer");
            issuerElement.setTextContent("http://localhost:8080/saml");
            authnRequest.appendChild(issuerElement);

            Element nameIDPolicyElement = document.createElementNS("urn:oasis:names:tc:SAML:2.0:protocol", "samlp:NameIDPolicy");
            nameIDPolicyElement.setAttribute("Format", "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
            authnRequest.appendChild(nameIDPolicyElement);

            document.appendChild(authnRequest);
            return document;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SAML request - AuthnRequest", e);
        }
    }
}

