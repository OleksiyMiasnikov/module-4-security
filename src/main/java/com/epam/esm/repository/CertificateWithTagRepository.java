package com.epam.esm.repository;

import com.epam.esm.model.entity.CertificateWithTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CertificateWithTagRepository extends JpaRepository<CertificateWithTag, Integer> {

    @Query("FROM CertificateWithTag c WHERE c.certificateId in :ids" )
    List<CertificateWithTag> findByCertificateId(@Param("ids") List<Integer> listOfCertificateId);

    @Query("FROM CertificateWithTag c WHERE c.tagId in :ids" )
    Page<CertificateWithTag> findByTagIds(@Param("ids") List<Integer> tagIds, Pageable pageable);
}
