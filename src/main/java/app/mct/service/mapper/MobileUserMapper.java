package app.mct.service.mapper;


import app.mct.domain.*;
import app.mct.service.dto.MobileUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MobileUser} and its DTO {@link MobileUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MobileUserMapper extends EntityMapper<MobileUserDTO, MobileUser> {



    default MobileUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        MobileUser mobileUser = new MobileUser();
        mobileUser.setId(id);
        return mobileUser;
    }
}
