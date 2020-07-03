package app.mct.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import app.mct.web.rest.TestUtil;

public class MobileUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileUserDTO.class);
        MobileUserDTO mobileUserDTO1 = new MobileUserDTO();
        mobileUserDTO1.setId(1L);
        MobileUserDTO mobileUserDTO2 = new MobileUserDTO();
        assertThat(mobileUserDTO1).isNotEqualTo(mobileUserDTO2);
        mobileUserDTO2.setId(mobileUserDTO1.getId());
        assertThat(mobileUserDTO1).isEqualTo(mobileUserDTO2);
        mobileUserDTO2.setId(2L);
        assertThat(mobileUserDTO1).isNotEqualTo(mobileUserDTO2);
        mobileUserDTO1.setId(null);
        assertThat(mobileUserDTO1).isNotEqualTo(mobileUserDTO2);
    }
}
