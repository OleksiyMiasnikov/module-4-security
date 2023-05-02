package com.epam.esm.repository;

import com.epam.esm.model.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByNameContaining(String pattern);

    List<Certificate> findByDescriptionContaining(String pattern);
}
