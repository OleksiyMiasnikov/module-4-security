package com.epam.esm.repository;

import com.epam.esm.model.entity.MostlyUsedTagIdByUserId;
import com.epam.esm.model.entity.UserOrder;
import com.epam.esm.model.entity.UserWithMaxTotalCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Integer> {

    List<UserOrder> findByUserId(int id);

    @Query("""
           select
                u.userId as userId,
                sum(u.cost) as totalCost
            from UserOrder u
            group by u.userId
            order by totalCost DESC
            limit 1
            """)
    UserWithMaxTotalCost findUsersWithTotalCost();

    @Query("""
            select
                cwt.tagId as tagId,
                count(cwt) as countTag
            from
                CertificateWithTag cwt,
                (
                    select u.CertificateWithTagId AS id
                    from UserOrder u
                    where u.userId = ?1
                ) uo
                where cwt.id = uo.id
            group by cwt.tagId
            order by countTag DESC
            limit 1
            """)
    MostlyUsedTagIdByUserId findMostlyUsedTag(int userId);
}
