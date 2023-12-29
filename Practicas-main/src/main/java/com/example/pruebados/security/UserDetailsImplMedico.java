package com.example.pruebados.security;

import com.example.pruebados.modelo.Medico;
import com.example.pruebados.modelo.Paciente;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserDetailsImplMedico implements UserDetails {

    private final Medico medico;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    public String getNombre (){
        return medico.getNombre();
    }
    @Override
    public String getPassword() {
        return medico.getPassword();
    }

    @Override
    public String getUsername() {
        return medico.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }




}
