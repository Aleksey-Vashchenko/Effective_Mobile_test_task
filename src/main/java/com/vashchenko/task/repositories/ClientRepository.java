package com.vashchenko.task.repositories;

import com.vashchenko.task.dto.views.ClientBaseProjection;
import com.vashchenko.task.entities.ClientUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientUser, UUID>, JpaSpecificationExecutor<ClientBaseProjection> {

    @Modifying
    @Query(value = "DELETE FROM user_phone_numbers WHERE user_id = :userId AND phone_numbers = :phoneNumber", nativeQuery = true)
    void deletePhoneNumberByUserIdAndPhoneNumber(@Param("userId") UUID userId, @Param("phoneNumber") String phoneNumber);

    @Query(value = "SELECT CASE WHEN COUNT(pn) > 0 THEN TRUE ELSE FALSE END FROM user_phone_numbers pn WHERE pn.phone = :phoneNumber", nativeQuery = true)
    boolean isPhoneNumberExists(@Param("phoneNumber") String phoneNumber);

    @Query(value = "SELECT CASE WHEN COUNT(em) > 0 THEN TRUE ELSE FALSE END FROM user_emails em WHERE em.email = :email",nativeQuery = true)
    boolean isEmailExists(@Param("email") String email);

    @Query(value = "SELECT CASE WHEN COUNT(us) > 0 THEN TRUE ELSE FALSE END FROM def_users us WHERE us.login = :login",nativeQuery = true)
    boolean isLoginExists(@Param("login") String login);

    Optional<ClientUser> findByLogin(String login);

    @Query("select cl.fullName as fullName, cl.birthDate as birthDate, " +
            "cl.phoneNumbers as phoneNumbers, cl.emails as emails from ClientUser cl")
    Page<ClientBaseProjection> findAll(Specification<ClientBaseProjection> spec, Pageable pageable);
}
