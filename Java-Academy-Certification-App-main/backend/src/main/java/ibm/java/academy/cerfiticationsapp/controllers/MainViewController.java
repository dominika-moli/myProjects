package ibm.java.academy.cerfiticationsapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ibm.java.academy.cerfiticationsapp.repository.UserJpaRepository;

@Controller
@RequestMapping("my-view")
public class MainViewController {
    @Autowired
    UserJpaRepository userRepo;

    @GetMapping("/all")
    public String listAll(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "listAll";
    }
}
