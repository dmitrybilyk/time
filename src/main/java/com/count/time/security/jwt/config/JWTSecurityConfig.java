package com.count.time.security.jwt.config;

import com.count.time.security.AuthProvider;
import com.count.time.security.CustomOidcUserService;
import com.count.time.security.jwt.JwtConfigurer;
import com.count.time.security.jwt.JwtTokenProvider;
import com.count.time.services.AuctionUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration class for JWT based Spring Security application.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@Configuration
//@EnableWebSecurity
//public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {
public class JWTSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

        private final AuctionUserDetailsService userDetailsService;

    private final AuthProvider authProvider;

    private final CustomOidcUserService oidcUserService;

    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";

    @Autowired
    public JWTSecurityConfig(JwtTokenProvider jwtTokenProvider, AuctionUserDetailsService userDetailsService, AuthProvider authProvider, CustomOidcUserService oidcUserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.authProvider = authProvider;
        this.oidcUserService = oidcUserService;
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/registration", "/css/**", "/v2/api-docs",
//                        "/swagger-resources/configuration/ui", "/swagger-resources",
//                        "/swagger-resources/configuration/security", "/swagger-ui.html",
//                        "/webjars/**", "/actuator/prometheus", "/actuator/prometheus/api/v1/query", "/**").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .oauth2Login()
////                .redirectionEndpoint()
////                .baseUri("/oauth2/callback/*")
////                .and()
//                .userInfoEndpoint()
//                .oidcUserService(oidcUserService)
//                .and()
//                .loginPage("/login")
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .successForwardUrl("/index")
//                .and().httpBasic()
//                .and()
//                .logout()
//                .permitAll()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login")
//                .and()
////                .and().authenticationProvider(authProvider)
//                .cors().and().csrf().disable();
//    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)
//    {
//        auth.authenticationProvider(authProvider);
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
