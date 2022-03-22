package com.example.demo1.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    JwtConfig jwtConfig;

    public JwtTokenVerifier(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String autharizationHeader = request.getHeader(jwtConfig.getAuthariationHeadder());
        if (!Strings.isNullOrEmpty(autharizationHeader) && autharizationHeader.startsWith(jwtConfig.getTokenPrefix() + " ")) {
            String token = autharizationHeader.substring((jwtConfig.getTokenPrefix() + " ").length());

            Jws<Claims> jws;
            String key = jwtConfig.getSecretKey();
            try {
                jws = Jwts.parserBuilder()
                        .setSigningKey(key.getBytes())
                        .build()
                        .parseClaimsJws(token);

                Claims body = jws.getBody();
                String userNme = body.getSubject();
                var authorities = (List<Map<String, String>>) body.get("authorities");

                var authoritesMapped = authorities.stream()
                        .map(authMap -> new SimpleGrantedAuthority(authMap.get("authority"))).collect(Collectors.toList());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userNme, null, authoritesMapped);

                // set authentication for each request
                // security context is the security info associated with each thread of execution/ each request
                // 1 request is served by a individual Thread of execution
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                filterChain.doFilter(request, response);
                // we can safely trust the JWT
            } catch (JwtException ex) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new IllegalStateException("Token cannot be verified !!!  :" + token);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
