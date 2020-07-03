package app.mct.web.rest;

import app.mct.service.EmailAlreadyUsedException;
import app.mct.service.MobileUserService;
import app.mct.web.rest.errors.BadRequestAlertException;
import app.mct.service.dto.MobileUserDTO;
import app.mct.service.dto.MobileUserCriteria;
import app.mct.service.MobileUserQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link app.mct.domain.MobileUser}.
 */
@RestController
@RequestMapping("/api")
public class MobileUserResource {

    private final Logger log = LoggerFactory.getLogger(MobileUserResource.class);

    private static final String ENTITY_NAME = "mobileUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MobileUserService mobileUserService;

    private final MobileUserQueryService mobileUserQueryService;

    public MobileUserResource(MobileUserService mobileUserService, MobileUserQueryService mobileUserQueryService) {
        this.mobileUserService = mobileUserService;
        this.mobileUserQueryService = mobileUserQueryService;
    }

    /**
     * {@code POST  /mobile-users} : Create a new mobileUser.
     *
     * @param mobileUserDTO the mobileUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mobileUserDTO, or with status {@code 400 (Bad Request)} if the mobileUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mobile-users")
    public ResponseEntity<MobileUserDTO> createMobileUser(@Valid @RequestBody MobileUserDTO mobileUserDTO) throws URISyntaxException {
        log.debug("REST request to save MobileUser : {}", mobileUserDTO);
        if (mobileUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new mobileUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MobileUserDTO result = mobileUserService.save(mobileUserDTO);
        return ResponseEntity.created(new URI("/api/mobile-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /mobile-users} : Create a new mobileUser.
     *
     * @param mobileUserDTO the mobileUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mobileUserDTO, or with status {@code 400 (Bad Request)} if the mobileUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mobileregister")
    public ResponseEntity<MobileUserDTO> registerMobileUser(@Valid @RequestBody MobileUserDTO mobileUserDTO) throws URISyntaxException {
        log.debug("REST request to save MobileUser : {}", mobileUserDTO);
        if (mobileUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new mobileUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MobileUserDTO result = mobileUserService.save(mobileUserDTO);
        return ResponseEntity.created(new URI("/api/mobile-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mobile-users} : Updates an existing mobileUser.
     *
     * @param mobileUserDTO the mobileUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mobileUserDTO,
     * or with status {@code 400 (Bad Request)} if the mobileUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mobileUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mobile-users")
    public ResponseEntity<MobileUserDTO> updateMobileUser(@Valid @RequestBody MobileUserDTO mobileUserDTO) throws URISyntaxException {
        log.debug("REST request to update MobileUser : {}", mobileUserDTO);
        if (mobileUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MobileUserDTO result = mobileUserService.save(mobileUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mobileUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mobile-users} : get all the mobileUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mobileUsers in body.
     */
    @GetMapping("/mobile-users")
    public ResponseEntity<List<MobileUserDTO>> getAllMobileUsers(MobileUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MobileUsers by criteria: {}", criteria);
        Page<MobileUserDTO> page = mobileUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mobile-users/count} : count all the mobileUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mobile-users/count")
    public ResponseEntity<Long> countMobileUsers(MobileUserCriteria criteria) {
        log.debug("REST request to count MobileUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(mobileUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mobile-users/:id} : get the "id" mobileUser.
     *
     * @param id the id of the mobileUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mobileUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mobile-users/{id}")
    public ResponseEntity<MobileUserDTO> getMobileUser(@PathVariable Long id) {
        log.debug("REST request to get MobileUser : {}", id);
        Optional<MobileUserDTO> mobileUserDTO = mobileUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mobileUserDTO);
    }

    /**
     * {@code DELETE  /mobile-users/:id} : delete the "id" mobileUser.
     *
     * @param id the id of the mobileUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mobile-users/{id}")
    public ResponseEntity<Void> deleteMobileUser(@PathVariable Long id) {
        log.debug("REST request to delete MobileUser : {}", id);
        mobileUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
