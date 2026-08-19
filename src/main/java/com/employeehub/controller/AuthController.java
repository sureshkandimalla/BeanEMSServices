package com.employeehub.controller;

import com.employeehub.config.JwtService;
import com.employeehub.config.TenantContext;
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

import java.util.HashMap;
import java.util.Map;

// Login is the one place a Google ID token is verified server-side — once,
// here, via Google's own tokeninfo endpoint (no extra crypto dependency
// needed; a plain REST call is enough at login volume). Only
// @beaninfosys.com / @intellanit.com / @intellan.com / @kksoftwareassociates.com
// emails get an app token back; every other domain is rejected outright, so a
// forged/edited localStorage entry on the frontend can't grant access the way
// the old client-only auth could — AuthFilter re-verifies THIS token's
// signature on every later request instead of trusting anything the client
// merely claims.
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private static final String GOOGLE_CLIENT_ID = "34277343649-4552ljfqc3ccsipco8jbf7jr3mu279j0.apps.googleusercontent.com";

    private static final Map<String, String> DOMAIN_TO_TENANT = Map.of(
            "beaninfosys.com", TenantContext.BEAN,
            "intellanit.com", TenantContext.INTELLAN,
            "intellan.com", TenantContext.INTELLAN,
            "kksoftwareassociates.com", TenantContext.KKASSOCIATES
    );

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

        String domain = email.substring(email.indexOf('@') + 1).toLowerCase();
        String tenant = DOMAIN_TO_TENANT.get(domain);
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
