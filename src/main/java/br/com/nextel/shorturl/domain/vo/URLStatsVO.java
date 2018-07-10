package br.com.nextel.shorturl.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class URLStatsVO {

	private Long hits;
	private Long urlCount;
	private List<URLVO> topUrls;

	public URLStatsVO(final Long hits, final Long urlCount) {
		this.hits = hits;
		this.urlCount = urlCount;
	}

	public void addTopUrl(final String systemURL, final URLVO vo) {
		if(this.topUrls == null)
			this.topUrls = new ArrayList<>();

		vo.setShortUrl(systemURL + "/" + vo.getShortUrl());
		this.topUrls.add(vo);
	}
}