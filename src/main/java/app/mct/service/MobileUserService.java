package app.mct.service;

import app.mct.service.dto.MobileUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link app.mct.domain.MobileUser}.
 */
public interface MobileUserService {

    /**
     * Save a mobileUser.
     *
     * @param mobileUserDTO the entity to save.
     * @return the persisted entity.
     */
    MobileUserDTO save(MobileUserDTO mobileUserDTO);

    /**
     * Get all the mobileUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MobileUserDTO> findAll(Pageable pageable);


    /**
     * Get the "id" mobileUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MobileUserDTO> findOne(Long id);

    /**
     * Delete the "id" mobileUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
