package app.mct.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import app.mct.domain.MobileUser;
import app.mct.domain.*; // for static metamodels
import app.mct.repository.MobileUserRepository;
import app.mct.service.dto.MobileUserCriteria;
import app.mct.service.dto.MobileUserDTO;
import app.mct.service.mapper.MobileUserMapper;

/**
 * Service for executing complex queries for {@link MobileUser} entities in the database.
 * The main input is a {@link MobileUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MobileUserDTO} or a {@link Page} of {@link MobileUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MobileUserQueryService extends QueryService<MobileUser> {

    private final Logger log = LoggerFactory.getLogger(MobileUserQueryService.class);

    private final MobileUserRepository mobileUserRepository;

    private final MobileUserMapper mobileUserMapper;

    public MobileUserQueryService(MobileUserRepository mobileUserRepository, MobileUserMapper mobileUserMapper) {
        this.mobileUserRepository = mobileUserRepository;
        this.mobileUserMapper = mobileUserMapper;
    }

    /**
     * Return a {@link List} of {@link MobileUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MobileUserDTO> findByCriteria(MobileUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MobileUser> specification = createSpecification(criteria);
        return mobileUserMapper.toDto(mobileUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MobileUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MobileUserDTO> findByCriteria(MobileUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MobileUser> specification = createSpecification(criteria);
        return mobileUserRepository.findAll(specification, page)
            .map(mobileUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MobileUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MobileUser> specification = createSpecification(criteria);
        return mobileUserRepository.count(specification);
    }

    /**
     * Function to convert {@link MobileUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MobileUser> createSpecification(MobileUserCriteria criteria) {
        Specification<MobileUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MobileUser_.id));
            }
            if (criteria.getMobileNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobileNumber(), MobileUser_.mobileNumber));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), MobileUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), MobileUser_.lastName));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), MobileUser_.dateOfBirth));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), MobileUser_.gender));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), MobileUser_.email));
            }
        }
        return specification;
    }
}
