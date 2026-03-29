package org.example.hotel_system.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.hotel_system.Autho.UserRepository;
import org.example.hotel_system.Enum.Role;
import org.example.hotel_system.Security.JwtUtil;
import org.example.hotel_system.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String provider = determineProvider(authentication);

        // Find or create the user in your database
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .role(Role.GUEST)
                            .provider(provider)
                            .providerId(oauthUser.getName())
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtUtil.generateToken(user);

        getRedirectStrategy().sendRedirect(request, response,
                "http://localhost:3000/oauth2/callback?token=" + token);
    }

    private String determineProvider(Authentication auth) {
        if (auth instanceof OAuth2AuthenticationToken token) {
            return token.getAuthorizedClientRegistrationId(); // "google" or "github"
        }
        return "unknown";
    }
}