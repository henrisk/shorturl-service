package br.com.nextel.shorturl.domain.repository;

import br.com.nextel.shorturl.domain.entity.URLEntity;
import br.com.nextel.shorturl.domain.vo.URLStatsVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface URLRepository extends CrudRepository<URLEntity, Long> {

	URLEntity findFirstByShortUrl(final String shortUrl);

	URLEntity findFirstByUrl(final String shortUrl);

	@Query("SELECT new br.com.linx.shorturl.domain.vo.URLStatsVO(SUM(url.hit), COUNT(url.url)) FROM URLEntity url")
	URLStatsVO getURLStats();

	List<URLEntity> findTop10ByOrderByHitDesc();

	@Query("SELECT new br.com.linx.shorturl.domain.vo.URLStatsVO(SUM(url.hit), COUNT(url.url)) FROM URLEntity url WHERE url.user.id = :userId")
	URLStatsVO getURLStatsByUser(@Param("userId") final Long userId);

	@Query("SELECT url FROM URLEntity url WHERE url.user.id = :userId ORDER BY url.hit")
	List<URLEntity> findTopByUserOrderByHitDesc(@Param("userId") final Long userId, Pageable page);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE URLEntity url SET url.hit = (url.hit + 1) WHERE url.id = :id")
	void hitURL(@Param("id") final Long id);
}