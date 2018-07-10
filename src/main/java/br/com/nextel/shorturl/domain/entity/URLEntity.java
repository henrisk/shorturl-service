package br.com.nextel.shorturl.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "URL")
public class URLEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "URL", nullable = false)
	private String url;

	@Column(name = "shortURL", nullable = false)
	private String shortUrl;

	@Column(name = "hit", nullable = false)
	private Long hit;

	@JoinColumn(name = "userId", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;
}