package com.example.pruebados.restApi;

import com.example.pruebados.Dtos.dto.PacienteDto;
import com.example.pruebados.modelo.Paciente;
import com.example.pruebados.service.PacienteServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/paciente")
public class PacienteControlador {

    private final PacienteServicio pacienteServicio;
    //esto es el cosntructor
    public PacienteControlador(PacienteServicio pacienteServicio) {
        this.pacienteServicio = pacienteServicio;
    }


//    @PostMapping("/signup")
//    public Paciente crearPaciente(@RequestBody Paciente paciente){
//        return pacienteServicio.crearPaciente(paciente);
//    }
//    //te da la lista
    @PostMapping("/signup")
    public ResponseEntity<String> agregarUsuario(@RequestBody Paciente paciente) {

        if(paciente.getPassword() != null && !paciente.getPassword().equalsIgnoreCase("")){



        if(paciente.getEmail() != null && !paciente.getEmail().equalsIgnoreCase("")){
         Optional<Paciente> pacienteExistente = pacienteServicio.findByEmail(paciente.getEmail());

        if (pacienteExistente.isPresent()) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya existe en la base de datos, porfavor vuelva a intentarlo con otro E-mail. Gracias");

        } else {


            Pattern pat=null;
            Matcher mat=null;

            //generamos las reglas de validacion.
            pat=Pattern.compile("[a-zA-Z0-9]{3,15}@[a-zA-Z]{1,8}\\.[a-zA-Z]{1,3}");
            //guardamos el valor en una variable de tipo mat para luego comprobarla
            mat=pat.matcher(paciente.getEmail());


            //envia x2
            if(mat.matches()==true){

                String contrasenaCifrada = new BCryptPasswordEncoder().encode(paciente.getPassword());
                paciente.setPassword(contrasenaCifrada);
                pacienteServicio.crearPaciente(paciente);
                return ResponseEntity.ok("Paciente creado exitosamente");
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El formato de E-mail es incorrecto. porfavor introduzca un email valido. Gracias");

            }
        }

        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El formato de E-mail esta vacio. porfavor introduzca un email. Gracias");

        }
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La contrasena esta vacia. Porfavor introduzca la contraseña. Gracias");
        }
    }
    //te da la lista


    @GetMapping("/lista")
    public List<Paciente> getTodosPacientes(){
        return pacienteServicio.getTodosPaciente();
    }
    //te devuelve los id
    @GetMapping("/{id}")
    public Optional<Paciente> getPacienteById(@PathVariable int id){
        return pacienteServicio.getPacienteById(id);}


    //--------------------------parte de los DTOS-----------------------------
    @GetMapping("/dtos")
    public List<PacienteDto> getTodosLosDtos(){
        return pacienteServicio.getTodosPacienteDtos();
    }

}
