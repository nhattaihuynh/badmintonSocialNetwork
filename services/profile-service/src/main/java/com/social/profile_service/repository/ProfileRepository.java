package com.social.profile_service.repository;

import com.social.profile_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByUsername(String username);
    boolean existsByUsername(String username);
    
    @Query("SELECT p FROM Profile p WHERE " +
           "LOWER(p.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.bio) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Profile> searchProfiles(String keyword);
}
