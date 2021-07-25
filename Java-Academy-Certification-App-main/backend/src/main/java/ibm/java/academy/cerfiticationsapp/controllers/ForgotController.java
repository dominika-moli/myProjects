package ibm.java.academy.cerfiticationsapp.controllers;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ibm.java.academy.cerfiticationsapp.model.User;
import ibm.java.academy.cerfiticationsapp.service.UserService;
import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/forgot_password")
    @ResponseBody
    public void processForgotPassword(@RequestParam(name = "email") String email) {
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = "http://localhost:8081/resetPassword/" + token;
            sendEmail(email, resetPasswordLink);

        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }

    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Reset Password Link!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText(
                "You have requested to reset your password. Click the link below to change your password: " + link);

        mailSender.send(mailMessage);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    @ResponseBody
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(user, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "Password changed. Go back to main page";
    }

    @PostMapping(path = "/changepassword")
    @ResponseBody
    public void resetPasswordOnFront(@RequestParam(name = "token") String token, @RequestParam(name = "password") String password) {

        User user = userService.getByResetPasswordToken(token);

        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("User with this reset token cannot be found.", token));
        } else {
            userService.updatePassword(user, password);
        }

    }
}
