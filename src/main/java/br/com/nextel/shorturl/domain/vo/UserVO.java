package br.com.nextel.shorturl.domain.vo;

import br.com.nextel.shorturl.domain.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVO {

	private Long id;
	private String name;

	public UserEntity toEntity() {
		return new UserEntity(this.getId(), this.getName());
	}

	public static UserVO fromEntity(UserEntity user) {
		return new UserVO(user.getId(), user.getName());
	}
}