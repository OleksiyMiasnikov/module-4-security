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
public interface CertificateWithTagRepository extends JpaRepository<CertificateWithTag, Long> {

    @Query("FROM CertificateWithTag c WHERE c.certificateId in :ids" )
    Page<CertificateWithTag> findByCertificateIds(@Param("ids") List<Long> listOfCertificateId, Pageable pageable);

    @Query("SELECT c.tagId FROM CertificateWithTag c WHERE c.certificateId = :id" )
    List<Long> findByCertificateId(Long id);

    @Query("FROM CertificateWithTag c WHERE c.tagId in :ids" )
    Page<CertificateWithTag> findByTagIds(@Param("ids") List<Long> tagIds, Pageable pageable);

    @Query("SELECT c.certificateId FROM CertificateWithTag c WHERE c.tagId in :ids" )
    List<Long> findCertificateIdsByTagIds(@Param("ids") List<Long> tagIds);

    void deleteByCertificateId(Long certificateId);

    CertificateWithTag findByCertificateIdAndTagId(Long certificateId, Long tagId);
}
