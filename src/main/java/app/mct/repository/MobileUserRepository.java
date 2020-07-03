package app.mct.repository;

import app.mct.domain.MobileUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * Spring Data  repository for the MobileUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MobileUserRepository extends JpaRepository<MobileUser, Long>, JpaSpecificationExecutor<MobileUser> {
    Optional<MobileUser> findOneByMobileNumber(String mobileNumber);

    Optional<MobileUser> findOneByEmail(String email);
}
