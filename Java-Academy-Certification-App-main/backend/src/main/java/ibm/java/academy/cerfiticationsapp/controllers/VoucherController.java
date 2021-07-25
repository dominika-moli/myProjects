package ibm.java.academy.cerfiticationsapp.controllers;

import ibm.java.academy.cerfiticationsapp.model.Voucher;
import ibm.java.academy.cerfiticationsapp.repository.VoucherJpaRepository;
import ibm.java.academy.cerfiticationsapp.service.UserService;
import ibm.java.academy.cerfiticationsapp.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("vouchers")
@EnableScheduling
public class VoucherController {

    @Autowired
    VoucherService voucherService;

    @Autowired
    UserService userService;

    @Autowired
    VoucherJpaRepository voucherJpaRepository;


    @PutMapping(value = "/activate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> activate(@RequestBody Voucher voucherRequest, @RequestParam("id") Long id) {
        try {
            Voucher voucher = voucherService.activate(id);
          userService.sendEmailVoucherActive(voucher);
            return new ResponseEntity<>(voucher, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/deactivate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> deactivate(@RequestBody Voucher voucherRequest, @RequestParam("id") Long id) {
        try {
            Voucher voucher = voucherService.updateState(id, Voucher.State.PENDING);
            return new ResponseEntity<>(voucher, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/certByVoucher/{voucherId}")
    @ResponseBody
    public Long getReviews(@PathVariable(name = "voucherId") Long voucherId) {
        Long id = voucherService.getCertByVoucher(voucherId);
        return id;
}

    @DeleteMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam("id") Long id) {
        try {
            voucherService.deleteVoucher(id);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // @PostMapping(value = "/activate", produces = "application/json")
    // public ResponseEntity<?> activate(@RequestParam("id") Long id) {
    //     try {
    //         Voucher voucher = voucherService.updateState(id, Voucher.State.ACTIVE);
    //         userService.sendEmailVoucherActive(voucher);
    //         return new ResponseEntity<>(voucher, HttpStatus.OK);
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }

    @Scheduled(fixedRate = 43200000)
    @GetMapping(value = "/notify-users")
    @ResponseBody
    public void notifyUsers(){
        voucherService.notifyUsers();
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addNew(@RequestBody Voucher voucher,
                                    @RequestParam("certId") Long certId,
                                    @RequestParam("userId") Long userId)
    {
        try {
            voucher = voucherService.create(voucher, certId, userId);
            return new ResponseEntity<>(voucher, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

}
