package com.example.demo1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        UserDetails annaSmith = User.builder()
                .username("annasmith")
                .password(passwordEncoder.encode("samaga"))
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthority()).build();
//                .roles(ApplicationUserRole.STUDENT.name())

        UserDetails linda = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("adminpassword"))
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthority())
//                .roles(ApplicationUserRole.ADMIN.name())
                .build();
        UserDetails tom = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
                .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthority())
                .build();
//                .roles(ApplicationUserRole.ADMINTRAINEE.name());

        auth.inMemoryAuthentication()
                .withUser(annaSmith)
                .withUser(linda)
                .withUser(tom);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // spring excepts "X-XSRF-TOKEN" as header in the request to be passed to prevent CSRF attack.
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPrmission())
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPrmission())
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPrmission())
//                .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll().defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key("secure-key")
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID","remember-me")
                .logoutSuccessUrl("/login");

    }

}
