package ibm.java.academy.cerfiticationsapp.service;

import ibm.java.academy.cerfiticationsapp.model.Certification;
import ibm.java.academy.cerfiticationsapp.model.Certification.State;
import ibm.java.academy.cerfiticationsapp.model.Skill;
import ibm.java.academy.cerfiticationsapp.model.Voucher;
import ibm.java.academy.cerfiticationsapp.repository.CertificationJpaRepository;
import ibm.java.academy.cerfiticationsapp.repository.SkillJpaRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Log4j2
public class CertificationService {

    @Autowired
    private CertificationJpaRepository certRepo;

    @Autowired
    private SkillJpaRepository skillRepo;


    public Certification saveCertification(@Valid Certification certification) {

        certification = certRepo.save(certification);
        return certification;

    }

    @Transactional
    public Certification updateAndSave(Long certId, String name, List<Long> skillIds) {
        Certification certification = null;
        Optional<Certification> certOpt = certRepo.findById(certId);
        if (certOpt.isPresent()) {
            Certification cert = certOpt.get();
            if (StringUtils.hasText(name)) {
                cert.setName(name);
            }
            if (!CollectionUtils.isEmpty(skillIds)) {
                List<Skill> foundSkills = skillRepo.findAllByIdIn(skillIds);
                if (!CollectionUtils.isEmpty(foundSkills)) {
                    cert.setSkills(foundSkills);
                }
            }
            certification = saveCertification(cert);
            log.info("Certification modified: " + certification.toString());
        }
        return certification;
    }

    public BigDecimal getSumPrice() {
        List<Certification> allCerts = certRepo.findAll();
        BigDecimal sum = allCerts.stream().filter(x -> x.getPrice() != null).map(x -> x.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum;
    }
    public Certification edit(Long id, String name, String url, BigDecimal price, String currency, State state) {
        Certification certification = null;
        Optional<Certification> certOpt = certRepo.findById(id);

        if (certOpt.isPresent()) {
            certification = certOpt.get();
            certification.setName(name);
            certification.setUrl(url);
            certification.setPrice(price);
            certification.setCurrency(currency);
            certification.setState(state);
            for (Voucher voucher : certification.getVouchers()) {
                voucher.setCertification(certification);
                voucher.setVoucherCode("VC" + voucher.getId() + "(" + certification.getName() + ")");
            }
            certification = certRepo.save(certification);
        } else {
            throw new IllegalArgumentException("Certification with the given id not found.");
        }

        return certification;
    }


    public List<Long> allVouchersUsers(Long id) {
        List<Long> result = new ArrayList<>();
        Certification certification;
        Optional<Certification> certificationOptional = certRepo.findById(id);

        if (certificationOptional.isPresent()) {
            certification = certificationOptional.get();
            List<Voucher> vouchers = certification.getVouchers();
            if (!vouchers.isEmpty())
                vouchers.forEach(voucher -> {
                    if (voucher.getUser() != null)
                        result.add(voucher.getUser().getId());
                });
        } else {
            throw new IllegalArgumentException("Certification with the given id not found.");
        }

        return result;
    }

    public Certification addSkill(Long id, Long skillId) {
        Skill skill;
        Certification certification = null;
        Optional<Skill> skillOptional = skillRepo.findById(skillId);
        Optional<Certification> certificationOptional = certRepo.findById(id);

        if (skillOptional.isPresent() && certificationOptional.isPresent()) {
            skill = skillOptional.get();
            certification = certificationOptional.get();
            certification.getSkills().add(skill);
            skill.getCertifications().add(certification);
            skill = skillRepo.save(skill);
            certification = certRepo.save(certification);
        }

        return certification;
    }

    public Certification deleteSkills(Long id) {
        Certification certification = null;
        Optional<Certification> certificationOptional = certRepo.findById(id);

        if (certificationOptional.isPresent()) {
            certification = certificationOptional.get();
            for (Skill skill : certification.getSkills()) {
                skill.getCertifications().remove(certification);
                skill = skillRepo.save(skill);
            }
            certification.setSkills(new ArrayList<>());
            certification = certRepo.save(certification);
        }

        return certification;
    }


    // public Certification assignSkills() {
    
    // }
}
