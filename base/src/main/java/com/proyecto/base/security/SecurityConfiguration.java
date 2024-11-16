package com.proyecto.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public SecurityConfiguration(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configuración de encabezados
        http.headers().frameOptions().sameOrigin();

        // Configuración de CORS
        http.cors()
            .configurationSource(request -> {
                org.springframework.web.cors.CorsConfiguration cors = new org.springframework.web.cors.CorsConfiguration();
                cors.setAllowedOrigins(java.util.Arrays.asList("http://frontend-dominio.com"));
                cors.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE"));
                cors.setAllowedHeaders(java.util.Arrays.asList("Authorization", "Content-Type", "X-CSRF-TOKEN"));
                cors.setAllowCredentials(true);
                return cors;
            });

        // Configuración de CSRF
        http.csrf()
            .csrfTokenRepository(cookieCsrfTokenRepository())
            .ignoringAntMatchers("/h2-console/**");

        // Configuración de rutas
        http.authorizeRequests()
            .antMatchers("/js/**", "/css/**", "/img/**", "/h2-console/**").permitAll()
            .antMatchers("/restablecer-password").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/procesa_login")
                .failureUrl("/login?error=true")
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler(customAuthenticationFailureHandler())
            .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }
    private CsrfTokenRepository cookieCsrfTokenRepository() {
    	  CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
          repository.setCookieHttpOnly(true);
          return repository;
	}

	@Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
