package com.employeehub.controller;

import com.employeehub.config.TenantContext;
import com.employeehub.config.TenantDefinition;
import com.employeehub.config.TenantRegistryProperties;
import com.employeehub.config.JwtService;
import com.employeehub.model.UserRole;
import com.employeehub.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

// Login is the one place a Google ID token is verified server-side — once,
// here, via Google's own tokeninfo endpoint (no extra crypto dependency
// needed; a plain REST call is enough at login volume). Only an email whose
// domain or exact address appears in the externalized tenant registry (see
// TenantRegistryProperties / application.properties' app.tenants[N] entries)
// gets an app token back; every other domain is rejected outright, so a
// forged/edited localStorage entry on the frontend can't grant access the way
// the old client-only auth could — AuthFilter re-verifies THIS token's
// signature on every later request instead of trusting anything the client
// merely claims.
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private static final String GOOGLE_CLIENT_ID = "34277343649-4552ljfqc3ccsipco8jbf7jr3mu279j0.apps.googleusercontent.com";

    @Autowired
    private TenantRegistryProperties tenantRegistry;

    // Built once at startup from the registry rather than per-request —
    // domain/email -> tenant key, both keyed lowercase.
    private Map<String, String> domainToTenant;
    private Map<String, String> emailToTenant;

    @PostConstruct
    void buildLookupMaps() {
        domainToTenant = new HashMap<>();
        emailToTenant = new HashMap<>();
        for (TenantDefinition tenant : tenantRegistry.getTenants()) {
            for (String domain : tenant.getDomains()) {
                domainToTenant.put(domain.toLowerCase(), tenant.getKey());
            }
            for (String email : tenant.getEmails()) {
                emailToTenant.put(email.toLowerCase(), tenant.getKey());
            }
        }
    }

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public static class LoginRequest {
        public String credential;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.credential == null || request.credential.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing credential"));
        }

        Map<String, Object> tokenInfo;
        try {
            tokenInfo = restTemplate.getForObject(
                    "https://oauth2.googleapis.com/tokeninfo?id_token=" + request.credential, Map.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Google token"));
        }

        if (tokenInfo == null || !GOOGLE_CLIENT_ID.equals(tokenInfo.get("aud"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Google token"));
        }

        String email = (String) tokenInfo.get("email");
        if (email == null || !email.contains("@")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Google token"));
        }

        String lowerEmail = email.toLowerCase();
        String domain = lowerEmail.substring(lowerEmail.indexOf('@') + 1);
        String tenant = emailToTenant.getOrDefault(lowerEmail, domainToTenant.get(domain));
        if (tenant == null) {
            logger.warn("Login rejected for unauthorized domain: {}", domain);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Your email isn't part of an authorized company"));
        }

        String appToken = jwtService.issueToken(email, tenant);

        // /api/v1/auth/login is excluded from AuthFilter (see its
        // shouldNotFilter), so TenantContext is never set for this request —
        // set it here ourselves, same as AuthFilter does for every other
        // request, so the routing datasource picks the right tenant's schema
        // for this lookup. A user with no row here simply gets role=null,
        // meaning "no access assigned yet" on the frontend.
        String role = null;
        TenantContext.set(tenant);
        try {
            role = userRoleRepository.findByEmailIgnoreCase(email).map(UserRole::getRole).orElse(null);
        } finally {
            TenantContext.clear();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("token", appToken);
        response.put("tenant", tenant);
        response.put("email", email);
        response.put("name", tokenInfo.get("name"));
        response.put("picture", tokenInfo.get("picture"));
        response.put("role", role);
        return ResponseEntity.ok(response);
    }
}
