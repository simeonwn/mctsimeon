package app.mct.web.rest;

import app.mct.MctApp;
import app.mct.domain.MobileUser;
import app.mct.repository.MobileUserRepository;
import app.mct.service.MobileUserService;
import app.mct.service.dto.MobileUserDTO;
import app.mct.service.mapper.MobileUserMapper;
import app.mct.service.dto.MobileUserCriteria;
import app.mct.service.MobileUserQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.mct.domain.enumeration.Gender;
/**
 * Integration tests for the {@link MobileUserResource} REST controller.
 */
@SpringBootTest(classes = MctApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MobileUserResourceIT {

    private static final String DEFAULT_MOBILE_NUMBER = "+6281161833519";
    private static final String SECOND_MOBILE_NUMBER = "+6281161833510";
    private static final String UPDATED_MOBILE_NUMBER = "08287709926";
    private static final String INVALID_MOBILE_NUMBER = "98287709926";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_EMAIL = "hG80a9@9IIlAk.ih.Nvg";
    private static final String SECOND_EMAIL = "hG80a0@9IIlAk.ih.Nvg";
    private static final String UPDATED_EMAIL = "EBA@KLz.R3z.3aq.hw.2h";
    private static final String INVALID_EMAIL = "EBA@KLz.R3z.3aq.hw.2hsds";

    @Autowired
    private MobileUserRepository mobileUserRepository;

    @Autowired
    private MobileUserMapper mobileUserMapper;

    @Autowired
    private MobileUserService mobileUserService;

    @Autowired
    private MobileUserQueryService mobileUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMobileUserMockMvc;

    private MobileUser mobileUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileUser createEntity(EntityManager em) {
        MobileUser mobileUser = new MobileUser()
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .email(DEFAULT_EMAIL);
        return mobileUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileUser createUpdatedEntity(EntityManager em) {
        MobileUser mobileUser = new MobileUser()
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL);
        return mobileUser;
    }

    @BeforeEach
    public void initTest() {
        mobileUser = createEntity(em);
    }

    @Test
    @Transactional
    public void checkRegisterMobileUser() throws Exception {
        int databaseSizeBeforeCreate = mobileUserRepository.findAll().size();
        // Create the MobileUser
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);
        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isCreated());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeCreate + 1);
        MobileUser testMobileUser = mobileUserList.get(mobileUserList.size() - 1);
        assertThat(testMobileUser.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testMobileUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMobileUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMobileUser.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testMobileUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMobileUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void checkRegisterMobileUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mobileUserRepository.findAll().size();

        // Create the MobileUser with an existing ID
        mobileUser.setId(1L);
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMobileNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set the field null
        mobileUser.setMobileNumber(null);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);


        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvalidMobileNumber() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set with invalid value
        mobileUser.setMobileNumber(INVALID_MOBILE_NUMBER);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);


        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMobileNumberIsUnique() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // Create the MobileUser
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);
        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isCreated());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest + 1);
        MobileUser testMobileUser = mobileUserList.get(mobileUserList.size() - 1);
        assertThat(testMobileUser.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testMobileUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMobileUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMobileUser.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testMobileUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMobileUser.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // set other unique field except mobile number
        mobileUser.setEmail(SECOND_EMAIL);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO2 = mobileUserMapper.toDto(mobileUser);

        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO2)))
            .andExpect(status().isBadRequest());

        mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set the field null
        mobileUser.setFirstName(null);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);


        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set the field null
        mobileUser.setLastName(null);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);


        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set the field null
        mobileUser.setEmail(null);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);


        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvalidEmail() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // set with invalid value
        mobileUser.setEmail(INVALID_EMAIL);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);


        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsUnique() throws Exception {
        int databaseSizeBeforeTest = mobileUserRepository.findAll().size();
        // Create the MobileUser
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);
        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isCreated());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest + 1);
        MobileUser testMobileUser = mobileUserList.get(mobileUserList.size() - 1);
        assertThat(testMobileUser.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testMobileUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMobileUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMobileUser.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testMobileUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMobileUser.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // set other unique field except email
        mobileUser.setMobileNumber(SECOND_MOBILE_NUMBER);

        // Create the MobileUser, which fails.
        MobileUserDTO mobileUserDTO2 = mobileUserMapper.toDto(mobileUser);

        restMobileUserMockMvc.perform(post("/api/mobileregister")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO2)))
            .andExpect(status().isBadRequest());

        mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    @Transactional
    public void getAllMobileUsers() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList
        restMobileUserMockMvc.perform(get("/api/mobile-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void getMobileUser() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get the mobileUser
        restMobileUserMockMvc.perform(get("/api/mobile-users/{id}", mobileUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mobileUser.getId().intValue()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }


    @Test
    @Transactional
    public void getMobileUsersByIdFiltering() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        Long id = mobileUser.getId();

        defaultMobileUserShouldBeFound("id.equals=" + id);
        defaultMobileUserShouldNotBeFound("id.notEquals=" + id);

        defaultMobileUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMobileUserShouldNotBeFound("id.greaterThan=" + id);

        defaultMobileUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMobileUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMobileUsersByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where mobileNumber equals to DEFAULT_MOBILE_NUMBER
        defaultMobileUserShouldBeFound("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the mobileUserList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultMobileUserShouldNotBeFound("mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByMobileNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where mobileNumber not equals to DEFAULT_MOBILE_NUMBER
        defaultMobileUserShouldNotBeFound("mobileNumber.notEquals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the mobileUserList where mobileNumber not equals to UPDATED_MOBILE_NUMBER
        defaultMobileUserShouldBeFound("mobileNumber.notEquals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where mobileNumber in DEFAULT_MOBILE_NUMBER or UPDATED_MOBILE_NUMBER
        defaultMobileUserShouldBeFound("mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER);

        // Get all the mobileUserList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultMobileUserShouldNotBeFound("mobileNumber.in=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where mobileNumber is not null
        defaultMobileUserShouldBeFound("mobileNumber.specified=true");

        // Get all the mobileUserList where mobileNumber is null
        defaultMobileUserShouldNotBeFound("mobileNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllMobileUsersByMobileNumberContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where mobileNumber contains DEFAULT_MOBILE_NUMBER
        defaultMobileUserShouldBeFound("mobileNumber.contains=" + DEFAULT_MOBILE_NUMBER);

        // Get all the mobileUserList where mobileNumber contains UPDATED_MOBILE_NUMBER
        defaultMobileUserShouldNotBeFound("mobileNumber.contains=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByMobileNumberNotContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where mobileNumber does not contain DEFAULT_MOBILE_NUMBER
        defaultMobileUserShouldNotBeFound("mobileNumber.doesNotContain=" + DEFAULT_MOBILE_NUMBER);

        // Get all the mobileUserList where mobileNumber does not contain UPDATED_MOBILE_NUMBER
        defaultMobileUserShouldBeFound("mobileNumber.doesNotContain=" + UPDATED_MOBILE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllMobileUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultMobileUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the mobileUserList where firstName equals to UPDATED_FIRST_NAME
        defaultMobileUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultMobileUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the mobileUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultMobileUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultMobileUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the mobileUserList where firstName equals to UPDATED_FIRST_NAME
        defaultMobileUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where firstName is not null
        defaultMobileUserShouldBeFound("firstName.specified=true");

        // Get all the mobileUserList where firstName is null
        defaultMobileUserShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMobileUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where firstName contains DEFAULT_FIRST_NAME
        defaultMobileUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the mobileUserList where firstName contains UPDATED_FIRST_NAME
        defaultMobileUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultMobileUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the mobileUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultMobileUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllMobileUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where lastName equals to DEFAULT_LAST_NAME
        defaultMobileUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the mobileUserList where lastName equals to UPDATED_LAST_NAME
        defaultMobileUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultMobileUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the mobileUserList where lastName not equals to UPDATED_LAST_NAME
        defaultMobileUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultMobileUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the mobileUserList where lastName equals to UPDATED_LAST_NAME
        defaultMobileUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where lastName is not null
        defaultMobileUserShouldBeFound("lastName.specified=true");

        // Get all the mobileUserList where lastName is null
        defaultMobileUserShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMobileUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where lastName contains DEFAULT_LAST_NAME
        defaultMobileUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the mobileUserList where lastName contains UPDATED_LAST_NAME
        defaultMobileUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultMobileUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the mobileUserList where lastName does not contain UPDATED_LAST_NAME
        defaultMobileUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultMobileUserShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the mobileUserList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultMobileUserShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultMobileUserShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the mobileUserList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultMobileUserShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultMobileUserShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the mobileUserList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultMobileUserShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth is not null
        defaultMobileUserShouldBeFound("dateOfBirth.specified=true");

        // Get all the mobileUserList where dateOfBirth is null
        defaultMobileUserShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultMobileUserShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the mobileUserList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultMobileUserShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultMobileUserShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the mobileUserList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultMobileUserShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultMobileUserShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the mobileUserList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultMobileUserShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultMobileUserShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the mobileUserList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultMobileUserShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllMobileUsersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where gender equals to DEFAULT_GENDER
        defaultMobileUserShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the mobileUserList where gender equals to UPDATED_GENDER
        defaultMobileUserShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where gender not equals to DEFAULT_GENDER
        defaultMobileUserShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the mobileUserList where gender not equals to UPDATED_GENDER
        defaultMobileUserShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultMobileUserShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the mobileUserList where gender equals to UPDATED_GENDER
        defaultMobileUserShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where gender is not null
        defaultMobileUserShouldBeFound("gender.specified=true");

        // Get all the mobileUserList where gender is null
        defaultMobileUserShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllMobileUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where email equals to DEFAULT_EMAIL
        defaultMobileUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the mobileUserList where email equals to UPDATED_EMAIL
        defaultMobileUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where email not equals to DEFAULT_EMAIL
        defaultMobileUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the mobileUserList where email not equals to UPDATED_EMAIL
        defaultMobileUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultMobileUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the mobileUserList where email equals to UPDATED_EMAIL
        defaultMobileUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where email is not null
        defaultMobileUserShouldBeFound("email.specified=true");

        // Get all the mobileUserList where email is null
        defaultMobileUserShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllMobileUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where email contains DEFAULT_EMAIL
        defaultMobileUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the mobileUserList where email contains UPDATED_EMAIL
        defaultMobileUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMobileUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        // Get all the mobileUserList where email does not contain DEFAULT_EMAIL
        defaultMobileUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the mobileUserList where email does not contain UPDATED_EMAIL
        defaultMobileUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMobileUserShouldBeFound(String filter) throws Exception {
        restMobileUserMockMvc.perform(get("/api/mobile-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restMobileUserMockMvc.perform(get("/api/mobile-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMobileUserShouldNotBeFound(String filter) throws Exception {
        restMobileUserMockMvc.perform(get("/api/mobile-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMobileUserMockMvc.perform(get("/api/mobile-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMobileUser() throws Exception {
        // Get the mobileUser
        restMobileUserMockMvc.perform(get("/api/mobile-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMobileUser() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        int databaseSizeBeforeUpdate = mobileUserRepository.findAll().size();

        // Update the mobileUser
        MobileUser updatedMobileUser = mobileUserRepository.findById(mobileUser.getId()).get();
        // Disconnect from session so that the updates on updatedMobileUser are not directly saved in db
        em.detach(updatedMobileUser);
        updatedMobileUser
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL);
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(updatedMobileUser);

        restMobileUserMockMvc.perform(put("/api/mobile-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isOk());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeUpdate);
        MobileUser testMobileUser = mobileUserList.get(mobileUserList.size() - 1);
        assertThat(testMobileUser.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testMobileUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMobileUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMobileUser.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testMobileUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMobileUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingMobileUser() throws Exception {
        int databaseSizeBeforeUpdate = mobileUserRepository.findAll().size();

        // Create the MobileUser
        MobileUserDTO mobileUserDTO = mobileUserMapper.toDto(mobileUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileUserMockMvc.perform(put("/api/mobile-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mobileUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MobileUser in the database
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMobileUser() throws Exception {
        // Initialize the database
        mobileUserRepository.saveAndFlush(mobileUser);

        int databaseSizeBeforeDelete = mobileUserRepository.findAll().size();

        // Delete the mobileUser
        restMobileUserMockMvc.perform(delete("/api/mobile-users/{id}", mobileUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        assertThat(mobileUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
