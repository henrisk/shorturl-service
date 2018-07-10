package br.com.nextel.shorturl.service.impl;

import br.com.nextel.shorturl.domain.entity.URLEntity;
import br.com.nextel.shorturl.domain.entity.UserEntity;
import br.com.nextel.shorturl.domain.repository.URLRepository;
import br.com.nextel.shorturl.domain.repository.UserRepository;
import br.com.nextel.shorturl.domain.vo.URLStatsVO;
import br.com.nextel.shorturl.domain.vo.URLVO;
import br.com.nextel.shorturl.service.URLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class URLServiceImpl implements URLService {

    private Logger logger = LoggerFactory.getLogger(URLServiceImpl.class);

    @Autowired
    private URLRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public URLVO addURL (URLVO url) {
        URLEntity urlEntity = urlRepository.findFirstByUrl(url.getUrl());
        if (urlEntity != null)
            return URLVO.fromEntity(urlEntity);

        Optional<UserEntity> user = userRepository.findById(url.getUserId());
        urlEntity = urlRepository.save(url.toEntity(user.get()));
        return URLVO.fromEntity(urlEntity);
    }

    @Override
    public URLVO findURL (String shortURL) {
        return URLVO.fromEntity(urlRepository.findFirstByShortUrl(shortURL));
    }

    @Override
    public URLVO findURLById (final Long id) {
        return URLVO.fromEntity(urlRepository.findById(id).get());
    }

    @Override
    public URLStatsVO getStats (final String systemURL) {
        URLStatsVO vo = urlRepository.getURLStats();
        List<URLEntity> listTop10 = urlRepository.findTop10ByOrderByHitDesc();
        if (listTop10 != null)
            listTop10.forEach(o -> vo.addTopUrl(systemURL, URLVO.fromEntity(o)));

        return vo;
    }

    @Override
    public URLStatsVO getStatsByUser (final String systemURL, final Long userId) {
        URLStatsVO vo = urlRepository.getURLStatsByUser(userId);
        List<URLEntity> top10 = urlRepository.findTopByUserOrderByHitDesc(userId, new PageRequest(0, 10));
        if (top10 != null)
            top10.forEach(o -> vo.addTopUrl(systemURL, URLVO.fromEntity(o)));

        return vo;
    }

    @Override
    public URLVO findById (final String systemURL, final Long id) {
        URLVO vo = URLVO.fromEntity(urlRepository.findById(id).get());
        vo.setShortUrl(systemURL + "/" + vo.getShortUrl());

        return vo;
    }

    @Override
    public void remove (final Long id) {
        urlRepository.deleteById(id);
    }

    @Override
    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public void hitURL (Long id) {
        urlRepository.hitURL(id);
    }
}