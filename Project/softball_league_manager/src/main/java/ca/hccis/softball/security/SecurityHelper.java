package ca.hccis.softball.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("securityHelper")
public class SecurityHelper {
    private static final Set<String> ALLOWED_EMAILS = Set.of(
            "bj.maclean@gmail.com",
            "developer@gmail.com"
    );

    public boolean hasAllowedEmail(Jwt jwt) {
        String email = jwt.getClaimAsString("email");

        // Accept any Gmail address.
        return email != null && email.endsWith("gmail.com");

        // Only accept one of the allowed emails.
        // return email != null && ALLOWED_EMAILS.contains(email);
    }
}