package com.example.demo1.security;

import com.example.demo1.auth.ApplicationUserService;
import com.example.demo1.jwt.JwtConfig;
import com.example.demo1.jwt.JwtTokenVerifier;
import com.example.demo1.jwt.JwtUserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        UserDetails annaSmith = User.builder()
//                .username("annasmith")
//                .password(passwordEncoder.encode("samaga"))
//                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthority()).build();
////                .roles(ApplicationUserRole.STUDENT.name())
//
//        UserDetails linda = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("adminpassword"))
//                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthority())
////                .roles(ApplicationUserRole.ADMIN.name())
//                .build();
//        UserDetails tom = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password123"))
//                .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthority())
//                .build();
////                .roles(ApplicationUserRole.ADMINTRAINEE.name());
//
//        auth.inMemoryAuthentication()
//                .withUser(annaSmith)
//                .withUser(linda)
//                .withUser(tom);
//    }


    @Override
    protected UserDetailsService userDetailsService() {
        return applicationUserService;
    }


    @Bean
    public DaoAuthenticationProvider getDAOAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(applicationUserService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getDAOAuthenticationProvider());
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
////                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                // spring excepts "X-XSRF-TOKEN" as header in the request to be passed to prevent CSRF attack.
//                .authorizeRequests()
//                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
//                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
////                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPrmission())
////                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPrmission())
////                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPrmission())
////                .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginPage("/login").permitAll().defaultSuccessUrl("/courses", true)
//                .and()
//                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                .rememberMeParameter("remember-me")
//                .rememberMeCookieName("remember-user-credentials")
//                .key("secure-key")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID", "remember-user-credentials")
//                .logoutSuccessUrl("/login");
//
//    }


    // WHEN USING JWT
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // making it statless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilter(new JwtUserAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig), JwtUserAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name());


    }
}
