package com.example.pruebados.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {




    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        AuthCredentials authCredentials = new AuthCredentials();
                try{
                authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
                }catch (IOException e){
                    System.out.println("Hola soy el error de JWT AUTENTICATION FILTER");
                }

        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
                authCredentials.getEmail(),
                authCredentials.getPassword(),
                Collections.emptyList()
        );

        return getAuthenticationManager().authenticate(usernamePAT);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


//-----------------------------------------AQUI HACE LA COMPROBACION ENTRE MEDICO Y PACIENTE-------------

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        if (userDetails instanceof UserDetailsImplPaciente) {

            System.out.println("ME METI COMO PACIENTE");
            UserDetailsImplPaciente userDetailsPaciente = (UserDetailsImplPaciente) userDetails;
            String token = TokenUtils.createToken(userDetailsPaciente.getNombre(), userDetailsPaciente.getUsername());
            response.addHeader("Authorization", "Bearer " + token);
            response.getWriter().flush();

        } else if (userDetails instanceof UserDetailsImplMedico) {
            System.out.println("ME METI COMO MEDICO");
            UserDetailsImplMedico userDetailsMedico = (UserDetailsImplMedico) userDetails;
            String token = TokenUtils.createToken(userDetailsMedico.getNombre(), userDetailsMedico.getUsername());
            response.addHeader("Authorization", "Bearer " + token);
            response.getWriter().flush();
        }

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
