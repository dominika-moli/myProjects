package ibm.java.academy.cerfiticationsapp.controllers;

import ibm.java.academy.cerfiticationsapp.model.Certification;
import ibm.java.academy.cerfiticationsapp.model.Certification.State;
import ibm.java.academy.cerfiticationsapp.repository.CertificationJpaRepository;
import ibm.java.academy.cerfiticationsapp.service.CertificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("certs")
class CertificationController {

    @Autowired
    CertificationJpaRepository certificationJpaRepository;

    @Autowired
    CertificationService certService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping(value = "/proposal", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addUser(@RequestBody Certification certificationRequest) {
        try {
            logger.info(certificationRequest.toString());
            Certification cert = certService.saveCertification(certificationRequest);
            return new ResponseEntity<>(cert, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Certification certification, @PathVariable Long id) {
        return certificationJpaRepository.findById(id).map(cert -> {
            cert.setName(certification.getName());
            cert.setPrice(certification.getPrice());
            cert = certificationJpaRepository.save(cert);
            return new ResponseEntity<>(cert, HttpStatus.OK);
        }).orElseGet(() -> {
            Certification cert = certificationJpaRepository.save(certification);
            return new ResponseEntity<>(cert, HttpStatus.CREATED);
        });
    }

    @PutMapping(value = "/edit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> edit(@RequestBody Certification certificationRequest,
                                  @RequestParam("id") Long id,
                                  @RequestParam("name") String name,
                                  @RequestParam("url") String url,
                                  @RequestParam(value = "price", required = false) BigDecimal price,
                                  @RequestParam("currency") String currency,
                                  @RequestParam(value = "state", required = false) State state)
    {
        try {
            Certification certification = certService.edit(id, name, url, price, currency, state);
            return new ResponseEntity<>(certification, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/students/{id}")
    @ResponseBody
    public List<Long> students(@PathVariable Long id) {
        return certService.allVouchersUsers(id);
    }

    @DeleteMapping(value = "/deleteSkills/{id}")
    public ResponseEntity<?> deleteSkills(@PathVariable("id") Long id) {
        try {
            Certification certification = certService.deleteSkills(id);
            return new ResponseEntity<>(certification, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/addSkill/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addSkill(@RequestBody Certification certificationRequest, @PathVariable("id") Long id, @RequestParam Long skillId) {
        try {
            Certification certification = certService.addSkill(id, skillId);
            return new ResponseEntity<>(certification, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public Certification getCertification() {
        Optional<Certification> certOpt = certificationJpaRepository.findById(10001L);
        Certification cert = certOpt.get();
        return cert;
    }
}
