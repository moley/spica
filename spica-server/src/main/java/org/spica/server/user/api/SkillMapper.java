package org.spica.server.user.api;

import java.util.ArrayList;
import java.util.List;
import org.spica.server.user.domain.Skill;
import org.spica.server.user.domain.User;
import org.spica.server.user.model.SkillInfo;
import org.spica.server.user.model.UserInfo;

public class SkillMapper {

    SkillInfo toApi (Skill skill) {
        SkillInfo skillInfo = new SkillInfo();
        skillInfo.setName(skill.getDescription());
        skillInfo.setId(skill.getId());
        return skillInfo;
    }

    List<SkillInfo> toApi (final List<Skill> allSkills, final String skillsForUser) {
        List<SkillInfo> skillInfos = new ArrayList<SkillInfo>();
        if (skillsForUser == null)
            return skillInfos;

        String [] skillsForUserArray = skillsForUser.split(",");
        for (String next: skillsForUserArray) {
            Skill nextSill = findSkill(allSkills, Integer.valueOf(next).intValue());
            skillInfos.add(toApi(nextSill));
        }

        return skillInfos;
    }

    List<SkillInfo> toApi (final List<Skill> allSkills) {
        List<SkillInfo> skillInfos = new ArrayList<SkillInfo>();
        for (Skill skill: allSkills) {
            skillInfos.add(toApi(skill));
        }

        return skillInfos;
    }

    Skill findSkill (final List<Skill> allSkills, final int skillId) {
        for (Skill next: allSkills) {
            if (next.getId() == skillId)
                return next;
        }

        throw new IllegalStateException("Skill " + skillId + " not found in the list of skills");

    }
}
