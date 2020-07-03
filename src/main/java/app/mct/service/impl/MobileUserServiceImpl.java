package app.mct.service.impl;

import app.mct.service.EmailAlreadyUsedException;
import app.mct.service.MobileNumberAlreadyUsedException;
import app.mct.service.MobileUserService;
import app.mct.domain.MobileUser;
import app.mct.repository.MobileUserRepository;
import app.mct.service.dto.MobileUserDTO;
import app.mct.service.mapper.MobileUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MobileUser}.
 */
@Service
@Transactional
public class MobileUserServiceImpl implements MobileUserService {

    private final Logger log = LoggerFactory.getLogger(MobileUserServiceImpl.class);

    private final MobileUserRepository mobileUserRepository;

    private final MobileUserMapper mobileUserMapper;

    public MobileUserServiceImpl(MobileUserRepository mobileUserRepository, MobileUserMapper mobileUserMapper) {
        this.mobileUserRepository = mobileUserRepository;
        this.mobileUserMapper = mobileUserMapper;
    }

    /**
     * Save a mobileUser.
     *
     * @param mobileUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MobileUserDTO save(MobileUserDTO mobileUserDTO) {
        log.debug("Request to save MobileUser : {}", mobileUserDTO);
        mobileUserRepository.findOneByMobileNumber(mobileUserDTO.getMobileNumber())
            .ifPresent(existingUser -> {
                throw new MobileNumberAlreadyUsedException();
            });
        mobileUserRepository.findOneByEmail(mobileUserDTO.getEmail())
            .ifPresent(existingUser -> {
                throw new EmailAlreadyUsedException();
            });
        MobileUser mobileUser = mobileUserMapper.toEntity(mobileUserDTO);
        mobileUser = mobileUserRepository.save(mobileUser);
        return mobileUserMapper.toDto(mobileUser);
    }

    /**
     * Get all the mobileUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MobileUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MobileUsers");
        return mobileUserRepository.findAll(pageable)
            .map(mobileUserMapper::toDto);
    }


    /**
     * Get one mobileUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MobileUserDTO> findOne(Long id) {
        log.debug("Request to get MobileUser : {}", id);
        return mobileUserRepository.findById(id)
            .map(mobileUserMapper::toDto);
    }

    /**
     * Delete the mobileUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MobileUser : {}", id);
        mobileUserRepository.deleteById(id);
    }
}
