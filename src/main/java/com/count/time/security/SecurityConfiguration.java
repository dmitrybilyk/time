//package com.count.time.security;
//
//import com.count.time.services.AuctionUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    private final AuctionUserDetailsService userDetailsService;
//
//    private final AuthProvider authProvider;
//
//    private final CustomOidcUserService oidcUserService;
//
//    public SecurityConfiguration(AuctionUserDetailsService userDetailsService, AuthProvider authProvider, CustomOidcUserService oidcUserService) {
//        this.userDetailsService = userDetailsService;
//        this.authProvider = authProvider;
//        this.oidcUserService = oidcUserService;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/registration", "/css/**", "/v2/api-docs",
//                        "/swagger-resources/configuration/ui", "/swagger-resources",
//                        "/swagger-resources/configuration/security", "/swagger-ui.html",
//                        "/webjars/**").permitAll()
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
//
//
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth)
////    {
////        auth.authenticationProvider(authProvider);
////    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
