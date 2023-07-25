package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityCouldNotBeDeletedException;
import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsDTO;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsRequest;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateWithTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.service.mapper.CertificateWithListOfTagsMapper;
import com.epam.esm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *  A service to work with {@link CertificateWithTag}.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateWithTagService{
    private final CertificateWithTagRepository repo;
    private final TagRepository tagRepo;
    private final CertificateRepository certificateRepo;
    private final CertificateMapper certificateMapper;
    private final CertificateWithListOfTagsMapper mapper;

//    /**
//     * Creates new record of certificate with tag.
//     * If tag doesn't exist, it will be created
//     *
//     * @param request - created certificate with tag request
//     * @return {@link CertificateWithTag} created tag
//     */
//    @Transactional
//    public CertificateWithTag create(CertificateWithTagRequest request) {
//        log.info("Creating a new certificate with tag.");
//
//        // if tag exists in the database, tagId get from database
//        // else a new tag will be created with new tagId
//        Long tagId;
//        Optional<Tag> optionalTag = tagRepo.findByName(request.getTags());
//        if (optionalTag.isEmpty()) {
//            Tag tag = Tag.builder()
//                    .name(request.getTags())
//                    .build();
//            tagId = tagRepo.save(tag).getId();
//        } else {
//            tagId = optionalTag.get().getId();
//        }
//
//        Certificate certificate = certificateMapper.toCertificate(request);
//        certificate.setCreateDate(DateUtil.getDate());
//        certificate.setLastUpdateDate(DateUtil.getDate());
//
//        Long certificateId = certificateRepo.save(certificate).getId();
//        CertificateWithTag certificateWithTag = CertificateWithTag.builder()
//                .tagId(tagId)
//                .certificateId(certificateId)
//                .build();
//        return repo.save(certificateWithTag);
//    }

    @Transactional
    public CertificateWithListOfTagsDTO create(CertificateWithListOfTagsRequest request) {
        log.info("Creating a new certificate with tag.");

        // if tag exists in the database, tagId get from database
        // else a new tag will be created with new tagId
        List<Long> tagsIds = getTagsIds(request.getTags());

        Certificate certificate = certificateMapper.toCertificate(request);
        certificate.setCreateDate(DateUtil.getDate());
        certificate.setLastUpdateDate(DateUtil.getDate());

        Long certificateId = certificateRepo.save(certificate).getId();

        for (Long tagsId : tagsIds) {
            repo.save(CertificateWithTag.builder()
                    .tagId(tagsId)
                    .certificateId(certificateId)
                    .build());
        }

        return findById(certificateId);
    }

    /**
     * Gets all certificates with list of tags by page.
     * Result will be selected by page and size.
     *
     * @param pageable page parameters
     * @return List of {@link CertificateWithTag} List of all certificates with tags from database
     */
    public Page<CertificateWithListOfTagsDTO> getAll(Pageable pageable) {
        log.info("Getting all certificates with list of tags.");
        return certificateRepo.findAll(pageable).map(mapper::toDTO);
    }

    /**
     * Finds all certificates by array of tags and part of name/description.
     *
     * @param pattern part of name/description
     * @param tags array with names of tags
     * @return List of {@link CertificateWithTag} List of all appropriate certificates with tags
     */
    @Transactional
    public Page<CertificateWithListOfTagsDTO> findByPartOfNameOrDescription(
            String pattern,
            String[] tags,
            Pageable pageable) {

        log.info("Getting all certificates by array of tags and pattern.");

        if (pattern.isEmpty()) {
            pattern = null;
        }

        return certificateRepo
                .findByListOfTagsAndPattern(tags, pattern, pageable)
                .map(mapper::toDTO);
    }

    /**
     * Finds a {@link CertificateWithTag} by its id.
     *
     * @param id certificate with tag id
     * @return {@link CertificateWithTag} CertificateWithTag
     * @throws if a certificate with tag with a given id doesn't exist throws {@link ApiEntityNotFoundException}
     */
    public CertificateWithListOfTagsDTO findById(Long id) {
        log.info("Locking for certificate with tags by id: {}.", id);
        Certificate certificate = certificateRepo.findById(id)
                .orElseThrow(() -> new ApiEntityNotFoundException(
                        "Requested certificate with tags was not found (id=" + id + ")"
                ));
        return mapper.toDTO(certificate);
    }

    @Transactional
    public CertificateWithListOfTagsDTO update(Long id, CertificateWithListOfTagsRequest request) {
        log.info("Updating certificate by id: {}.", id);

        Certificate certificate = certificateMapper.toCertificate(request);
        certificateRepo.findById(id)
                .ifPresent(value -> certificate.setCreateDate(value.getCreateDate()));
        certificate.setLastUpdateDate(DateUtil.getDate());
        certificateRepo.save(certificate);

        List<Long> oldTagsIds = repo.findByCertificateId(certificate.getId());
        List<Long> newTagsIds = getTagsIds(request.getTags());

        newTagsIds.stream()
                .filter(element -> !oldTagsIds.contains(element))
                .forEach((tagId) -> repo.save(CertificateWithTag.builder()
                        .certificateId(certificate.getId())
                        .tagId(tagId)
                        .build()));
        List<Long> difference = oldTagsIds.stream()
                .filter(element -> !newTagsIds.contains(element))
                        .toList();
        for (Long item: difference) {
            CertificateWithTag result = repo.findByCertificateIdAndTagId(certificate.getId(), item);
            repo.delete(result);
        }

        return findById(id);
    }

    private List<Long> getTagsIds(String[] tags) {
        List<Long> tagsIds = new ArrayList<>();
        for (String tagName : tags) {
            Optional<Tag> optionalTag = tagRepo.findByName(tagName);
            if (optionalTag.isEmpty()) {
                Tag tag = Tag.builder()
                        .name(tagName)
                        .build();
                tagsIds.add(tagRepo.save(tag).getId());
            } else {
                tagsIds.add(optionalTag.get().getId());
            }
        }
        return tagsIds;
    }

    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting certificate by id: {}.", id);
        Optional<Certificate> deletedCertificate = certificateRepo.findById(id);
        if (deletedCertificate.isPresent()) {
            try {
                certificateRepo.delete(deletedCertificate.get());
            } catch (Exception exception) {
                throw new ApiEntityCouldNotBeDeletedException(
                        "Cannot delete a parent row: a foreign key constraint fails"
                );
            }
            repo.deleteByCertificateId(id);
            return true;
        } else {
            return false;
        }
    }
}
