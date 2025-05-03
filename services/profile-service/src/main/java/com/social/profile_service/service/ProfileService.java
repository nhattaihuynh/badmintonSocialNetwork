package com.social.profile_service.service;

import com.social.profile_service.dto.ProfileRequest;
import com.social.profile_service.dto.ProfileResponse;
import com.social.profile_service.entity.Profile;
import com.social.profile_service.exception.ProfileAlreadyExistsException;
import com.social.profile_service.exception.ProfileNotFoundException;
import com.social.profile_service.mapper.ProfileMapper;
import com.social.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    
    @Transactional(readOnly = true)
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<ProfileResponse> getAllProfileResponses() {
        return profileRepository.findAll().stream()
                .map(profileMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Profile getProfileById(UUID id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public ProfileResponse getProfileResponseById(UUID id) {
        Profile profile = getProfileById(id);
        return profileMapper.toResponse(profile);
    }
    
    @Transactional(readOnly = true)
    public Profile getProfileByUsername(String username) {
        return profileRepository.findByUsername(username)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with username: " + username));
    }
    
    @Transactional(readOnly = true)
    public ProfileResponse getProfileResponseByUsername(String username) {
        Profile profile = getProfileByUsername(username);
        return profileMapper.toResponse(profile);
    }
    
    @Transactional
    public Profile createProfile(Profile profile) {
        if (profileRepository.existsByUsername(profile.getUsername())) {
            throw new ProfileAlreadyExistsException("Username already taken: " + profile.getUsername());
        }
        return profileRepository.save(profile);
    }
    
    @Transactional
    public ProfileResponse createProfileFromRequest(ProfileRequest profileRequest) {
        Profile profile = profileMapper.toEntity(profileRequest);
        Profile createdProfile = createProfile(profile);
        return profileMapper.toResponse(createdProfile);
    }
    
    @Transactional
    public Profile updateProfile(UUID id, Profile profileDetails) {
        Profile existingProfile = getProfileById(id);
        
        // Only update username if it's changed and not already taken by another user
        if (!existingProfile.getUsername().equals(profileDetails.getUsername()) && 
            profileRepository.existsByUsername(profileDetails.getUsername())) {
            throw new ProfileAlreadyExistsException("Username already taken: " + profileDetails.getUsername());
        }
        
        // Update fields using MapStruct
        profileMapper.updateProfileFromRequest(profileDetails, existingProfile);
        
        return profileRepository.save(existingProfile);
    }
    
    @Transactional
    public ProfileResponse updateProfileFromRequest(UUID id, ProfileRequest profileRequest) {
        Profile profileDetails = profileMapper.toEntity(profileRequest);
        Profile updatedProfile = updateProfile(id, profileDetails);
        return profileMapper.toResponse(updatedProfile);
    }
    
    @Transactional
    public void deleteProfile(UUID id) {
        if (!profileRepository.existsById(id)) {
            throw new ProfileNotFoundException("Profile not found with id: " + id);
        }
        profileRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Profile> searchProfiles(String keyword) {
        return profileRepository.searchProfiles(keyword);
    }
    
    @Transactional(readOnly = true)
    public List<ProfileResponse> searchProfileResponses(String keyword) {
        return searchProfiles(keyword).stream()
                .map(profileMapper::toResponse)
                .collect(Collectors.toList());
    }
}
