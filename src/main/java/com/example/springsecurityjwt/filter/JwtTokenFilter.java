package com.example.springsecurityjwt.filter;

import com.example.springsecurityjwt.configuration.UserDetailsServiceImp;
import com.example.springsecurityjwt.repository.UserRepository;
import com.example.springsecurityjwt.utils.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImp userDetailsServiceImp;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImp userDetailsServiceImp) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //GET AUTHORIZATION HEADER AND VALIDATE
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header.isEmpty() || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return

                    ;
        }

        final String jwtToken = header.split(" ")[1].trim();

        //GET USER IDENTITY
        UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(jwtTokenUtil.extractUsername(jwtToken));

        //GET JWT TOKEN AND VALIDATE
        if(!jwtTokenUtil.validateToken(jwtToken, userDetails)){
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
