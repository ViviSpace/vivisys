package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.Serv;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Serv entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServRepository extends JpaRepository<Serv,Long> {
    
    @Query("select distinct serv from Serv serv left join fetch serv.serviceProviders left join fetch serv.agents")
    List<Serv> findAllWithEagerRelationships();

    @Query("select serv from Serv serv left join fetch serv.serviceProviders left join fetch serv.agents where serv.id =:id")
    Serv findOneWithEagerRelationships(@Param("id") Long id);
    
}
