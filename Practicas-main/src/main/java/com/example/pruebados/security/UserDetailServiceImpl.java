package com.example.pruebados.security;

import com.example.pruebados.modelo.Medico;
import com.example.pruebados.modelo.Paciente;
import com.example.pruebados.repositorio.MedicoRepositorio;
import com.example.pruebados.repositorio.PacienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    PacienteRepositorio pacienteRepository;
    @Autowired
    MedicoRepositorio medicoRepositorio;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Paciente paciente = pacienteRepository.findByEmail(username).orElse(null);
        if (paciente != null) {
            System.out.println("Soy un paciente");
            return new UserDetailsImplPaciente(paciente);
        }

        Medico medico = medicoRepositorio.findByEmail(username).orElse(null);
        if (medico != null) {
            System.out.println("Soy un medico");
            return new UserDetailsImplMedico(medico);
        }

        throw new UsernameNotFoundException("El usuario con email "+ username +" no existe");


//     Paciente usuario = usuarioRepository
//                .findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email "+ username +" no existe"));
//
//     return new UserDetailsImplPaciente(usuario);

    }
}
