package com.proyecto.base.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.service.UsuarioService;

@Controller
public class LoginController {
	
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login( HttpServletRequest request,Model model) {
    	   HttpSession session = request.getSession();
           String errorMessage = (String) session.getAttribute("errorLogin");

           if (errorMessage != null) {
               model.addAttribute("errorLogin", errorMessage);
               session.removeAttribute("errorLogin");
           }
        model.addAttribute("loginForm", new UsuarioDTO());
        return "login";
    }
    
//    @PostMapping("/login")
//    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
//        System.out.println("Username: " + username);
//        System.out.println("Password: " + password);
//        model.addAttribute("error", "Invalid username or password");
//        return "login"; // Redirige de vuelta al formulario de login
//    }
    
    
    @PostMapping("/login")
    public  String login(@ModelAttribute("loginForm") UsuarioDTO usuarioDTO, Model model) {
    	
        ResponseDTO<UsuarioDTO> response = usuarioService.login(usuarioDTO);
        if (!response.isOk()) {
            model.addAttribute("error", response.getLog());
            return "login";
        }
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home() {
        return "home"; 
    }
    
    @GetMapping("/cambiar-password")
    public String cambiarPassword() {
        return "cambiar-password"; 
    }
    
    
    @PostMapping("/procesa_cambio_password")
    public String procesarCambioPassword(@RequestParam("newPassword") String newPassword,
                                         @RequestParam("confirmPassword") String confirmPassword,
                                         Principal principal, Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "cambiar-password";
        }
      if (usuarioService.cambiarContrasenia(principal.getName(), newPassword)) {
    	  return "redirect:/home";
      };
      
      return "cambiar-password";
    }
    
    @GetMapping("/restablecer-password")
    public String mostrarRestablecerPassword() {
        return "restablecer-contrasenia.html"; 
    }
    

    @PostMapping("/restablecer-password")
    public String restablecerPassword(@RequestParam String email, Model model) {
        if (usuarioService.restablecerContrasenia(email)) {
            model.addAttribute("message", "Se ha enviado una contraseña provisional a tu correo.");
        } else {
            model.addAttribute("error", "No se encontró un usuario con ese correo electrónico.");
        }
        return "login";
    }
}