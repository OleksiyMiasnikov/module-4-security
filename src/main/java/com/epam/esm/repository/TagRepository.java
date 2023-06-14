package com.epam.esm.repository;

import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findByName(String name, Pageable pageable);

    Optional<Tag> findByName(String name);

    @Query("SELECT t.name FROM Tag t WHERE t.id in :ids" )
    List<String> findByIds(@Param("ids") List<Long> tagIds);
}
