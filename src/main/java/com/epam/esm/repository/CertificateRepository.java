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

    @Query("FROM Certificate c WHERE c.id in :ids" )
    List<Certificate> findByIds(@Param("ids") List<Long> ids);

    /*
   ` select * from certificate
   ` where name like '%456%'
   ` OR description like '%JAVA%'
    OR id in (select certificate_id from certificate_with_tag
    where tag_id in (select id from tag where name in ('tag_1')))
     */

    @Query(
            "FROM Certificate c " +
            "WHERE c.name LIKE %:pattern% " +
            "OR c.description LIKE %:pattern% " +
                    "OR c.id in:ids"
    )
    Page<Certificate> findByListOfTagsAndPattern(
            @Param("ids") List<Long> ids,
            String pattern,
            Pageable pageable);
}
