package br.com.nextel.shorturl.service;

import br.com.nextel.shorturl.domain.vo.URLStatsVO;
import br.com.nextel.shorturl.domain.vo.URLVO;

public interface URLService {

	URLVO addURL(URLVO url);

	URLVO findURL(String shortURL);

	URLVO findURLById(Long id);

	URLStatsVO getStats(final String shortURL);

	URLStatsVO getStatsByUser(final String systemURL, final Long userId);

	URLVO findById(final String systemURL, final Long id);

	void remove(final Long id);

	void hitURL(final Long id);
}