package ibm.java.academy.cerfiticationsapp.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ibm.java.academy.cerfiticationsapp.model.User;
import ibm.java.academy.cerfiticationsapp.repository.UserJpaRepository;

@Controller
public class MainRestController {

    @RequestMapping(method = RequestMethod.GET, path = "hello")
    // @GetMapping("hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "lang", required = false) String lang) {
        if (lang != null && lang.equalsIgnoreCase("en")) {
            return "Hi " + (name == null ? "Anonymous" : name);
        } else {
            return "Ahoj " + (name == null ? "Anonymous" : name);
        }
    }

    @GetMapping(path = "/hi/{name}/*/{age}")
    @ResponseBody
    public String helloPathVariable(@PathVariable(name = "name") String name,
            @PathVariable(name = "age", required = false) Integer age) {
        return "Cau " + name + ". Age: " + age;
    }

    @Autowired
    UserJpaRepository userRepo;

    @GetMapping("/all-users")
    @ResponseBody
    public List<User> users() {
        return userRepo.findAll();
    }

    @PostMapping(path = "/add-user", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public User addUser(@RequestBody User user) {
        return userRepo.save(user);
    }

    @DeleteMapping("/delete-user")
    @ResponseBody
    public void deleteUser(@RequestParam("id") Long id) {
        userRepo.deleteAllById(Arrays.asList(id));
    }

}
