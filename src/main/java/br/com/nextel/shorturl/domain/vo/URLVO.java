package br.com.nextel.shorturl.domain.vo;

import br.com.nextel.shorturl.domain.entity.URLEntity;
import br.com.nextel.shorturl.domain.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class URLVO {

	private Long id;
	private Long hits;
	private String url;
	private String shortUrl;

	@JsonIgnore
	private Long userId;

	public URLEntity toEntity(final UserEntity user) {
		return new URLEntity(id, url, shortUrl, 0L, user);
	}

	public static URLVO fromEntity(URLEntity entity) {
		if(entity == null)
			return null;

		return new URLVO(entity.getId(), entity.getHit(), entity.getUrl(), entity.getShortUrl(), entity.getUser().getId());
	}
}