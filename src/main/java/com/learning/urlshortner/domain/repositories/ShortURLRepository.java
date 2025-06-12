package com.learning.urlshortner.domain.repositories;

import com.learning.urlshortner.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShortURLRepository extends JpaRepository<ShortUrl, Long> {

    // JPA derived method query name in this way to generate the query.
    // List<ShortUrl> findByIsPrivateIsFalseOrderByCreatedAtDesc();

    // JPQL query method standard method
    @Query("select su from ShortUrl su left join fetch su.createdBy where su.isPrivate = false order by su.createdAt desc")
    // @EntityGraph(attributePaths = {"createdBy"})  This also provides the same result as above left join
    //Or we can use DTO's to avoid the lazy loading, getting all the data to the DTO classes and then we can show it in the web.
    List<ShortUrl> findPublicShortUrls();

    boolean existsByShortKey(String shortKey);
}
