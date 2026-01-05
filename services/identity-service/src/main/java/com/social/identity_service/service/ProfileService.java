//package com.social.profile_service.service;
//
//import com.social.kafka.command.CreateProfileCommand;
//import com.social.profile_service.dto.ProfileRequest;
//import com.social.profile_service.dto.ProfileResponse;
//import com.social.profile_service.entity.Profile;
//import com.social.profile_service.exception.ProfileAlreadyExistsException;
//import com.social.profile_service.exception.ProfileNotFoundException;
//import com.social.profile_service.mapper.ProfileMapper;
//import com.social.profile_service.repository.ProfileRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class ProfileService {
//    private final ProfileRepository profileRepository;
//    private final ProfileMapper profileMapper;
////    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Transactional(readOnly = true)
//    public Profile getProfileById(UUID id) {
//        return profileRepository.findById(id)
//                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));
//    }
//
//    @Transactional(readOnly = true)
//    public ProfileResponse getProfileResponseById(UUID id) {
//        Profile profile = getProfileById(id);
//        return profileMapper.toResponse(profile);
//    }
//
//    @Transactional
//    public Profile createProfile(Profile profile) {
//        if (profileRepository.existsByUsername(profile.getUsername())) {
//            throw new ProfileAlreadyExistsException("Username already taken: " + profile.getUsername());
//        }
//        // send kafka event to friendship service to create a new profile
//        CreateProfileCommand event = new CreateProfileCommand(
//                profile.getId().toString(),
//                profile.getUsername(),
//                profile.getFullName()
//        );
////        kafkaTemplate.send("profile-create", event.profileId(), event)
////                .thenApply(result -> "Message published successfully with id: " + event.profileId());
//
//        return profileRepository.save(profile);
//    }
//
//    @Transactional
//    public ProfileResponse createProfileFromRequest(ProfileRequest profileRequest) {
//        Profile profile = profileMapper.toEntity(profileRequest);
//        Profile createdProfile = createProfile(profile);
//        return profileMapper.toResponse(createdProfile);
//    }
//}
