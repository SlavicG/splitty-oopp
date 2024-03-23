package server.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // For API projects, CSRF can usually be disabled.
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/admin/**").authenticated()  // Secure only /admin/** endpoints.
                        .anyRequest().permitAll())  // All other endpoints are openly accessible.
                .httpBasic(Customizer.withDefaults());  // Utilize HTTP Basic for authentication in this example.

        return http.build();
    }
}
