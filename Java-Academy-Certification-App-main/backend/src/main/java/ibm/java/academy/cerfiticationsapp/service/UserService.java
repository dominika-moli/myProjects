package ibm.java.academy.cerfiticationsapp.service;

import ibm.java.academy.cerfiticationsapp.model.ConfirmationToken;
import ibm.java.academy.cerfiticationsapp.model.Role;
import ibm.java.academy.cerfiticationsapp.model.User;
import ibm.java.academy.cerfiticationsapp.model.Voucher;

import ibm.java.academy.cerfiticationsapp.repository.RoleJpaRepository;

import ibm.java.academy.cerfiticationsapp.repository.UserJpaRepository;
import ibm.java.academy.cerfiticationsapp.repository.VoucherJpaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Log
public class UserService {

    private final UserJpaRepository userRepository;

    private final RoleJpaRepository roleRepository;

    private final VoucherJpaRepository voucherRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private JavaMailSender mailSender;

    public User loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }
    }

    public User signUpUser(User user) {

        if (user.getEmail() == null) {
            throw new BadCredentialsException(MessageFormat.format("Email required.", user.getEmail()));
        }

        final Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if (optionalUser.isPresent()) {
            throw new BadCredentialsException(MessageFormat.format("Email taken.", user.getEmail()));
        }

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        final User createdUser = userRepository.save(user);
        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // sendConfirmationMail(user.getEmail(),
        // confirmationToken.getConfirmationToken());
        return createdUser;
    }

    public ConfirmationToken signInUser(String email, String password) {

        final Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {

            if (bCryptPasswordEncoder.matches(password, optionalUser.get().getPassword())) {
                final ConfirmationToken confirmationToken = new ConfirmationToken(optionalUser.get());
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                return confirmationToken;
            }
            throw new BadCredentialsException(MessageFormat.format("Wrong password.", email));
        } else {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }

    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        final User optionalUser = loadUserByUsername(email);
        optionalUser.setResetPasswordToken(token);
        userRepository.save(optionalUser);

    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).get();
    }

    public void updatePassword(User user, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    public User updateRole(Long id, String roleId) {
        User result;
        Role newRole;

        Optional<User> userOpt = userRepository.findById(id);
        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            result = userOpt.get();
            newRole = roleOpt.get();
            newRole = roleRepository.save(newRole);
            result.setRole(newRole);
            result = userRepository.save(result);
        } else {
            throw new IllegalArgumentException("User with the given id not found.");
        }

        return result;
    }


    public void sendEmailVoucherActive(Voucher voucher) {
        String email = voucher.getUser().getEmail();
        String voucherCode = voucher.getVoucherCode();
        String voucherCertificationName = voucher.getCertification().getName();

        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your voucher has been activated");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText("Your voucher with code " + voucherCode + " for certification: " + voucherCertificationName +  " has been activated.");

        mailSender.send(mailMessage);

    }

    public void sendEmailVoucherExpires(Voucher currentVoucher) {        
        String email = currentVoucher.getUser().getEmail();
        log.info(email);
        String voucherCode = currentVoucher.getVoucherCode();
        log.info(voucherCode);
        String voucherCertificationName = currentVoucher.getCertification().getName();

        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your voucher expires soon");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText("Your voucher with code " + voucherCode + " for certification: " + voucherCertificationName +  " expires in less than a week!");

        mailSender.send(mailMessage);
    }

    public User addVoucher(Long id, Long voucherId) {
        User user;
        Voucher voucher;

        Optional<User> userOpt = userRepository.findById(id);
        Optional<Voucher> voucherOpt = voucherRepository.findById(voucherId);

        if (userOpt.isPresent() && voucherOpt.isPresent()) {
            user = userOpt.get();
            voucher = voucherOpt.get();
            voucher.setUser(user);
            voucher = voucherRepository.save(voucher);
            List<Voucher> vouchers = user.getVouchers();
            vouchers.add(voucher);
            user.setVouchers(vouchers);
            user = userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid id.");
        }

        return user;
    }

}
