package org.usco.parcialdos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login").permitAll();
                    auth.requestMatchers("/new").hasAuthority("RECTOR");
                    auth.requestMatchers("/delete/**").hasAuthority("RECTOR");
                    auth.requestMatchers("/edit/**").hasAuthority("RECTOR");
                    auth.requestMatchers("/change-time/**").hasAnyAuthority("RECTOR", "DOCENTE");
                    auth.requestMatchers("/home").authenticated();
                    auth.requestMatchers(HttpMethod.POST).hasAnyAuthority("RECTOR", "DOCENTE");
                    auth.requestMatchers(HttpMethod.GET).authenticated();
                    auth.requestMatchers(HttpMethod.DELETE).hasAuthority("RECTOR");
                    auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.permitAll();
                    login.loginPage("/login");
                    login.defaultSuccessUrl("/home", true);
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout");
                    logout.invalidateHttpSession(true);
                    logout.clearAuthentication(true);
                    logout.deleteCookies("JSESSIONID");
                })
                .exceptionHandling(ex -> {
                    ex.accessDeniedHandler(deniedHandler());
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImp userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AccessDeniedHandler deniedHandler() {
        return (request, response, auth) -> {
            response.sendRedirect("/fobidden");
        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Subject management")
                        .version("1.0")
                        .description("Subject Management system")
                        .termsOfService("http://swagger.io/terms/"));
    }
}
