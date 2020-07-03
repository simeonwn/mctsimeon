package app.mct.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import app.mct.web.rest.TestUtil;

public class MobileUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileUser.class);
        MobileUser mobileUser1 = new MobileUser();
        mobileUser1.setId(1L);
        MobileUser mobileUser2 = new MobileUser();
        mobileUser2.setId(mobileUser1.getId());
        assertThat(mobileUser1).isEqualTo(mobileUser2);
        mobileUser2.setId(2L);
        assertThat(mobileUser1).isNotEqualTo(mobileUser2);
        mobileUser1.setId(null);
        assertThat(mobileUser1).isNotEqualTo(mobileUser2);
    }
}
