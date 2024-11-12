package csd.backend.security;

// import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.authentication.AuthenticationManager;
// // import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// // import org.springframework.security.config.http.SessionCreationPolicy;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.oauth2.core.oidc.user.OidcUser;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// // import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// // import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.cors.CorsConfigurationSource;

// import csd.backend.filter.JwtRequestFilter;

// // import jakarta.servlet.http.HttpServletResponse;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     // @Autowired
//     // private JwtRequestFilter jwtRequestFilter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(matchmaking -> matchmaking
//                         .requestMatchers("/matchmaking/**").permitAll()
//                         .anyRequest().permitAll()
//                 )
//                 .sessionManagement(session -> session
//                     .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                 ); // Set stateless session

//         // http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     // For frontend, currently using localhost
//     // Should change if Frontend is deployed on a server
//     // @Bean
//     // public WebMvcConfigurer corsConfigurer() {
//     //     return new WebMvcConfigurer() {
//     //         @Override
//     //         public void addCorsMappings(CorsRegistry registry) {
//     //             registry.addMapping("/**") // Allow CORS for all paths
//     //                     .allowedOrigins(
//     //                         "http://cs203-bucket.s3-website-ap-southeast-1.amazonaws.com"
//     //                     ) // Allow specific origins
//     //                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
//     //                     .allowedHeaders("*") // Allow all headers
//     //                     .allowCredentials(true); // Allow cookies/credentials to be sent
//     //         }
//     //     };
//     // }
//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
//         configuration.addAllowedOrigin("*");
//         configuration.addAllowedMethod("*");
//         configuration.addAllowedHeader("*");
//         configuration.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }
// }

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // Use CORS settings with defaults
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().permitAll()) // Permit all requests without authentication
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless session

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Use addAllowedOriginPattern for wildcard support
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials if needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS settings to all paths
        return source;
    }
}

