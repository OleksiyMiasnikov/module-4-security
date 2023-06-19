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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Creates new record of certificate with tag.
     * If tag doesn't exist, it will be created
     *
     * @param request - created certificate with tag request
     * @return {@link CertificateWithTag} created tag
     */
    @Transactional
    public CertificateWithTag create(CertificateWithTagRequest request) {
        log.info("Creating a new certificate with tag.");

        // if tag exists in the database, tagId get from database
        // else a new tag will be created with new tagId
        Long tagId;
        Optional<Tag> optionalTag = tagRepo.findByName(request.getTags());
        if (optionalTag.isEmpty()) {
            Tag tag = Tag.builder()
                    .name(request.getTags())
                    .build();
            tagId = tagRepo.save(tag).getId();
        } else {
            tagId = optionalTag.get().getId();
        }

        Certificate certificate = certificateMapper.toCertificate(request);
        certificate.setCreateDate(DateUtil.getDate());
        certificate.setLastUpdateDate(DateUtil.getDate());

        Long certificateId = certificateRepo.save(certificate).getId();
        CertificateWithTag certificateWithTag = CertificateWithTag.builder()
                .tagId(tagId)
                .certificateId(certificateId)
                .build();
        return repo.save(certificateWithTag);
    }

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
     * Finds all certificates with tags by page.
     * Result will be selected by page and size.
     *
     * @param pageable page parameters
     * @return List of {@link CertificateWithTag} List of all certificates with tags from database
     */
    public Page<CertificateWithTag> findAll(Pageable pageable) {
        log.info("Getting all certificates with tags.");
        return repo.findAll(pageable);
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
        Page<CertificateWithListOfTagsDTO> result =
                certificateRepo.findAll(pageable).map(mapper::toDTO);
        for (CertificateWithListOfTagsDTO element: result.getContent()) {
            List<Long> tagsIds = repo.findByCertificateId(element.getId());
            List<String> tagsNames = tagRepo.findByIds(tagsIds);
            element.setTags(tagsNames);
        }
        return result;
    }

    /**
     * Finds all certificates by tags name.
     *
     * @param pageable page parameters
     * @param tagList list with tags
     * @return List of {@link CertificateWithTag} List of all certificates with appropriate tag
     */
    public Page<CertificateWithTag> findByTagNames(Pageable pageable, List<String> tagList) {
        log.info("Getting all certificates by tag.");
        List<Long> tagIds = new ArrayList<>();
        for (String name : tagList) {
            tagRepo.findByName(name).ifPresent(tag -> tagIds.add(tag.getId()));
        }
        return repo.findByTagIds(tagIds, pageable);
    }

//    /**
//     * Finds all certificates by part of name/description.
//     *
//     * @param pattern part of name/description
//     * @return List of {@link CertificateWithTag} List of all appropriate certificates with tags
//     */
//    public Page<CertificateWithTag> findByPartOfNameOrDescription(String pattern, Pageable pageable) {
//        log.info("Getting certificates by part of name or description.");
//
//        Set<Certificate> set = new HashSet<>(certificateRepo.findByNameContaining(pattern));
//        set.addAll(certificateRepo.findByDescriptionContaining(pattern));
//
//        List<Long> listOfCertificateId = new ArrayList<>(set.stream().map(Certificate::getId).toList());
//
//        return repo.findByCertificateIds(listOfCertificateId, pageable);
//    }

    /**
     * Finds all certificates by part of name/description.
     *
     * @param pattern part of name/description
     * @return List of {@link CertificateWithTag} List of all appropriate certificates with tags
     */
    public Page<CertificateWithListOfTagsDTO> findByPartOfNameOrDescription(String pattern, Pageable pageable) {
        log.info("Getting certificates by part of name or description.");

        Set<Certificate> set = new HashSet<>(certificateRepo.findByNameContaining(pattern));
        set.addAll(certificateRepo.findByDescriptionContaining(pattern));
        List<CertificateWithListOfTagsDTO> list = set.stream().map(mapper::toDTO).toList();

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        final Page<CertificateWithListOfTagsDTO> page =
                new PageImpl<>(list.subList(start, end), pageable, list.size());

        for (CertificateWithListOfTagsDTO element: page.getContent()) {
            List<Long> tagsIds = repo.findByCertificateId(element.getId());
            List<String> tagsNames = tagRepo.findByIds(tagsIds);
            element.setTags(tagsNames);
        }

        return page;
    }

    /**
     * Finds a {@link CertificateWithTag} by its id.
     *
     * @param id certificate with tag id
     * @return {@link CertificateWithTag} CertificateWithTag
     * @throws if a certificate with tag with a given id doesn't exist throws {@link ApiEntityNotFoundException}
     */
//    public CertificateWithTag findById(Long id) {
//        log.info("Locking for certificate with tag by id: {}.", id);
//
//        return repo.findById(id)
//                .orElseThrow(() -> new ApiEntityNotFoundException(
//                        "Requested certificate with tag was not found (id=" + id + ")"
//                        ));
//    }

    public CertificateWithListOfTagsDTO findById(Long id) {
        log.info("Locking for certificate with tags by id: {}.", id);
        Certificate certificate = certificateRepo.findById(id)
                .orElseThrow(() -> new ApiEntityNotFoundException(
                        "Requested certificate with tags was not found (id=" + id + ")"
                ));

        CertificateWithListOfTagsDTO result = mapper.toDTO(certificate);

        List<Long> tagsIds = repo.findByCertificateId(certificate.getId());
        List<String> tagsNames = tagRepo.findByIds(tagsIds);
        result.setTags(tagsNames);

        return result;
    }

    public CertificateWithListOfTagsDTO update(Long id, CertificateWithListOfTagsRequest request) {
        log.info("Updating certificate by id: {}.", id);

        Certificate certificate = certificateMapper.toCertificate(request);
        certificate.setCreateDate(certificateRepo.findById(id).get().getCreateDate());
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
                throw new ApiEntityCouldNotBeDeletedException("Cannot delete a parent row: a foreign key constraint fails");
            }
            repo.deleteByCertificateId(id);
            return true;
        } else {
            return false;
        }
    }
}
