package ibm.java.academy.cerfiticationsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ibm.java.academy.cerfiticationsapp.model.Skill;

public interface SkillJpaRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByIdIn(List<Long> skillIds);
}
