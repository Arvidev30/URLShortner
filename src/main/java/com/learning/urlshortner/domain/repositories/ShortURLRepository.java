package com.learning.urlshortner.domain.repositories;

import com.learning.urlshortner.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortURLRepository extends JpaRepository<ShortUrl, Long> {

    // JPA derived method query name in this way to generate the query.
    // List<ShortUrl> findByIsPrivateIsFalseOrderByCreatedAtDesc();

    // JPQL query method standard method
    @Query("select su from ShortUrl su left join fetch su.createdBy where su.isPrivate = false order by su.createdAt desc")
    // @EntityGraph(attributePaths = {"createdBy"})  This also provides the same result as above left join
    //Or we can use DTO's to avoid the lazy loading, getting all the data to the DTO classes and then we can show it in the web.
    List<ShortUrl> findPublicShortUrls();

    // @Query ("select count(su) > 0 from ShortUrl su where su.shortKey = :shortKey")
    boolean existsByShortKey(String shortKey);
    // Naming convention of spring jpa to check the count of the shorkey present, if >0 true else false

    Optional<ShortUrl> findByShortKey(String shortKey);
}
