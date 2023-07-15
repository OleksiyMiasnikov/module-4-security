package com.epam.esm.repository;

import com.epam.esm.model.entity.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByNameContaining(String pattern);

    List<Certificate> findByDescriptionContaining(String pattern);

    @Query(
            "FROM Certificate c " +
            "WHERE c.name LIKE %:pattern% " +
            "OR c.description LIKE %:pattern% " +
                    "OR c.id in (SELECT c.certificateId " +
                    "            FROM CertificateWithTag c " +
                    "            WHERE c.tagId in " +
                    "            (" +
                    "               SELECT t.id " +
                    "               FROM Tag t " +
                    "               WHERE t.name in :tags " +
                    "            ))"
    )
    Page<Certificate> findByListOfTagsAndPattern(
            @Param("tags") String[] tags,
            String pattern,
            Pageable pageable);
}
