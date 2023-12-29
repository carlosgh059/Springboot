package com.example.pruebados.restApi;

import com.example.pruebados.Dtos.dto.MedicoDto;
import com.example.pruebados.modelo.Medico;
import com.example.pruebados.modelo.Paciente;
import com.example.pruebados.service.MedicoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/medico")
public class MedicoControlador {

    private final MedicoServicio medicoServicio;


    public MedicoControlador(MedicoServicio medicoServicio) {
        this.medicoServicio = medicoServicio;
    }


//    @PostMapping("/signup")
//    public Medico crearPaciente(@RequestBody Medico medico){
//        return medicoServicio.crearMedico(medico);
//    }

    @PostMapping("/signup")
    public ResponseEntity<String> agregarUsuario(@RequestBody Medico medico) {


        if(medico.getPassword() != null && !medico.getPassword().equalsIgnoreCase("")){



            if(medico.getEmail() != null && !medico.getEmail().equalsIgnoreCase("")){
                Optional<Medico> medicoExistente = medicoServicio.findByEmail(medico.getEmail());

                if (medicoExistente.isPresent()) {

                    return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya existe en la base de datos, porfavor vuelva a intentarlo con otro E-mail. Gracias");

                } else {

                //-----comprobamos con expresion regulares
                    Pattern pat=null;
                    Matcher mat=null;

                    //generamos las reglas de validacion.
                    pat=Pattern.compile("[a-zA-Z0-9]{3,15}@[a-zA-Z]{1,8}\\.[a-zA-Z]{1,3}");
                    //guardamos el valor en una variable de tipo mat para luego comprobarla
                    mat=pat.matcher(medico.getEmail());


                    //envia x2
                    if(mat.matches()==true){

                        String contrasenaCifrada = new BCryptPasswordEncoder().encode(medico.getPassword());
                        medico.setPassword(contrasenaCifrada);
                        medicoServicio.crearMedico(medico);
                        return ResponseEntity.ok("Medico creado exitosamente");
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
    @GetMapping("/lista")
    public List<Medico> getTodosPacientes(){
        return medicoServicio.getTodosMedicos();
    }

    //te retorna el medico por ese id
    @GetMapping("/{id}")
    public Optional<Medico> getMedicoById(@PathVariable int id){
        return medicoServicio.getMedicoById(id);
    }

    //--------------------------parte de los DTOS-----------------------------
    @GetMapping("/dtos")
    public List<MedicoDto> getTodosMedicosDtos(){
        return medicoServicio.getTodosLosMedicosDtos();
    }
}
