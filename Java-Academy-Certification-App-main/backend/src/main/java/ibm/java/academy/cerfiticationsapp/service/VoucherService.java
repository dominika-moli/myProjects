package ibm.java.academy.cerfiticationsapp.service;


import ibm.java.academy.cerfiticationsapp.model.Certification;
import ibm.java.academy.cerfiticationsapp.model.User;
import ibm.java.academy.cerfiticationsapp.model.Voucher;
import ibm.java.academy.cerfiticationsapp.repository.CertificationJpaRepository;
import ibm.java.academy.cerfiticationsapp.repository.UserJpaRepository;
import ibm.java.academy.cerfiticationsapp.repository.VoucherJpaRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@Log
public class VoucherService {

    @Autowired
    private UserJpaRepository userRepo;

    @Autowired
    private VoucherJpaRepository voucherRepo;

    @Autowired
    UserService userService;

    @Autowired
    private CertificationJpaRepository certRepo;


    public Voucher assignVoucherToUser(Long voucherId, Long userId) {
        Voucher result = null;
        Optional<Voucher> voucherOpt = voucherRepo.findById(voucherId);
        Optional<User> userOpt = userRepo.findById(userId);
        if (voucherOpt.isPresent() && userOpt.isPresent()) {
            result = voucherOpt.get();
            result.setUser(userOpt.get());
            result = voucherRepo.save(result);
        }
        return result;
    }

    public void deleteVoucher(Long id) {
        voucherRepo.deleteById(id);
    }

    /**
     * Sets new state to voucher with the given id
     * 
     * @throws IllegalArgumentException if voucher with the given id is not found or
     *                                  if id is null
     * @return updated voucher
     */
    public Voucher activate(Long voucherId) {
        Voucher result = null;

        Optional<Voucher> voucherOpt = voucherRepo.findById(voucherId);

        if (voucherOpt.isPresent()) {
            result = voucherOpt.get();
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, 3);
            result.setValidUntil(c.getTime());
            result.setState(Voucher.State.ACTIVE);
            result = voucherRepo.save(result);
        } else {
            throw new IllegalArgumentException("Voucher with the given id was not found.");
        }

        return result;

    }

    public Voucher updateState(Long id, Voucher.State state) {
        Voucher result = null;

        Optional<Voucher> voucherOpt = voucherRepo.findById(id);

        if (voucherOpt.isPresent()) {
            result = voucherOpt.get();
            result.setState(state);
            result = voucherRepo.save(result);
        } else {
            throw new IllegalArgumentException("Voucher with the given id was not found.");
        }

        return result;
    }


    public Long getCertByVoucher(Long id) {
        Optional<Voucher> voucher = voucherRepo.findById(id);

        if (voucher.isPresent()) {
            return voucher.get().getCertification().getId();
        }

        else {
            throw new IllegalArgumentException("Voucher with the given id was not found.");
        }
    }


    public void notifyUsers() {
        Date date = new Date();
        log.info(date.toString());
        List<Voucher> voucherList = voucherRepo.findAll();

        for (int i = 0; i < voucherList.size(); i++) {
            Voucher currentVoucher = voucherList.get(i);

            long diffInMillies = Math.abs(currentVoucher.getValidUntil().getTime() - date.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff<= 7 && currentVoucher.getState() == Voucher.State.ACTIVE) {
                 userService.sendEmailVoucherExpires(currentVoucher);

            }

        }
    }

    public Voucher create(Voucher voucher, Long certId, Long userId) {
        Optional<Certification> certOpt = certRepo.findById(certId);
        Optional<User> userOpt = userRepo.findById(userId);

        if (certOpt.isPresent() && userOpt.isPresent()) {
            voucher.setCertification(certOpt.get());
            voucher.setUser(userOpt.get());
            voucher.setState(Voucher.State.PENDING);
            voucher.setValidUntil(null);
            voucher = voucherRepo.save(voucher);
            voucher.setVoucherCode("VC" + voucher.getId() + " (" + voucher.getCertification().getName() + ")");
            voucher = voucherRepo.save(voucher);
        } else {
            throw new IllegalArgumentException("Invalid id");
        }

        return voucher;
    }
}
