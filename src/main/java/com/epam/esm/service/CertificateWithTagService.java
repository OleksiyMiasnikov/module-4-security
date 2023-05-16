package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateWithTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Tag> tagList = tagRepo.findByName(request.getTag());
        if (tagList.size() == 0) {
            Tag tag = Tag.builder()
                    .name(request.getTag())
                    .build();
            tagId = tagRepo.save(tag).getId();
        } else {
            tagId = tagList.get(0).getId();
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
            List<Tag> tags = tagRepo.findByName(name);
            if (tags.size() > 0) tagIds.add(tags.get(0).getId());
        }
        return repo.findByTagIds(tagIds, pageable);
    }

    /**
     * Finds all certificates by part of name/description.
     *
     * @param pattern part of name/description
     * @return List of {@link CertificateWithTag} List of all appropriate certificates with tags
     */
    public Page<CertificateWithTag> findByPartOfNameOrDescription(String pattern, Pageable pageable) {
        log.info("Getting certificates by part of name or description.");

        Set<Certificate> set = new HashSet<>(certificateRepo.findByNameContaining(pattern));
        set.addAll(certificateRepo.findByDescriptionContaining(pattern));

        List<Long> listOfCertificateId = new ArrayList<>(set.stream().map(Certificate::getId).toList());

        return repo.findByCertificateId(listOfCertificateId, pageable);
    }

    /**
     * Finds a {@link CertificateWithTag} by its id.
     *
     * @param id certificate with tag id
     * @return {@link CertificateWithTag} CertificateWithTag
     * @throws if a certificate with tag with a given id doesn't exist throws {@link ApiEntityNotFoundException}
     */
    public CertificateWithTag findById(Long id) {
        log.info("Locking for certificate with tag by id: {}.", id);

        return repo.findById(id)
                .orElseThrow(() -> new ApiEntityNotFoundException(
                        "Requested certificate with tag was not found (id=" + id + ")"
                        ));
    }
}
