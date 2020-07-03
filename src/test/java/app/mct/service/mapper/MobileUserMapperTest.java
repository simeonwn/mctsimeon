package app.mct.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MobileUserMapperTest {

    private MobileUserMapper mobileUserMapper;

    @BeforeEach
    public void setUp() {
        mobileUserMapper = new MobileUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(mobileUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(mobileUserMapper.fromId(null)).isNull();
    }
}
