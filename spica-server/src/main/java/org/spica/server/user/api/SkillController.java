package org.spica.server.user.api;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.user.domain.Skill;
import org.spica.server.user.domain.SkillRepository;
import org.spica.server.user.model.SkillInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkillController implements SkillApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillController.class);


    @Autowired
    private SkillRepository skillRepository;

    private SkillMapper skillMapper = new SkillMapper();


    @Override public ResponseEntity<SkillInfo> createSkill(@NotNull @Valid String name) {
        LOGGER.info("create skill " + name);
        Skill newSkill = new Skill();
        newSkill.setDescription(name);
        skillRepository.save(newSkill);

        return new ResponseEntity<SkillInfo>(skillMapper.toApi(newSkill), HttpStatus.OK);
    }

    @Override public ResponseEntity<List<SkillInfo>> getSkills() {
        LOGGER.info("get all skills from database");
        List<Skill> all = skillRepository.findAll();
        List<SkillInfo> skillInfos = skillMapper.toApi(all);
        LOGGER.info("- found " + all.size() + " skills");
        return new ResponseEntity<List<SkillInfo>> (skillInfos, HttpStatus.OK);
    }
}
