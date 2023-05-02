package com.epam.esm.service;

import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.model.DTO.certificate.CreateCertificateRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 *  A service to work with {@link Certificate}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {

    private final CertificateRepository repo;
    private final CertificateMapper mapper;

    /**
     * Creates a new tag.
     *
     * @param request - creating certificate request
     * @return {@link Certificate} created certificate
     */
    public Certificate create(CreateCertificateRequest request) {
        log.info("Creating a new certificate.");

        Certificate certificate = mapper.toCertificate(request);

        return repo.save(certificate);
    }

    /**
     * Finds all certificates.
     *
     * @return List of {@link Certificate} List of all certificates from database
     */
    public Page<Certificate> findAll(Pageable pageable) {
        log.info("Getting all certificates.");
        return repo.findAll(pageable);
    }

    /**
     * Finds a {@link Certificate} by its id.
     *
     * @param id certificate id
     * @return {@link Certificate} certificate
     * @throws CertificateNotFoundException if a certificate with a given id doesn't exist
     */
    public Certificate findById(Long id) {
        log.info("Locking for certificate by id: {}.", id);

        return repo.findById(id)
                .orElseThrow(() ->
                        new CertificateNotFoundException(
                                "Requested certificate is not found (id=" + id + ")"
                        ));
    }

    /**
     * Updates a {@link Certificate} by its id.
     * Updates only fields, that present(not null)
     *
     * @param id certificate id
     * @param certificate fields to update
     * @return {@link Certificate} updated certificate
     */
    @Transactional
    public Certificate update(Long id, Certificate certificate) {
        log.info("Updating certificate by id: {}.", id);

        Certificate oldCertificate = findById(id);

        if (certificate.getName() != null) {
            oldCertificate.setName(certificate.getName());
        }
        if (certificate.getDescription() != null) {
            oldCertificate.setDescription(certificate.getDescription());
        }
        if (certificate.getPrice() != null) {
            oldCertificate.setPrice(certificate.getPrice());
        }
        if (certificate.getDuration() != null) {
            oldCertificate.setDuration(certificate.getDuration());
        }
        oldCertificate.setLastUpdateDate(DateUtil.getDate());

        return repo.save(oldCertificate);
    }

    /**
     * Removes a {@link Certificate} by its id.
     *
     * @param id certificate id
     * @return boolean result of removing certificate with appropriate id
     */
    public boolean delete(Long id) {
        log.info("Deleting certificate by id: {}.", id);
        Optional<Certificate> deletedCertificate = repo.findById(id);
        if (deletedCertificate.isPresent()) {
            repo.delete(deletedCertificate.get());
            return true;
        } else {
            return false;
        }
    }
}
