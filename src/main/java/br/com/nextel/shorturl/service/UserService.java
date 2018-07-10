package br.com.nextel.shorturl.service;

import br.com.nextel.shorturl.domain.vo.UserVO;

public interface UserService {
	boolean isExistingUser(Long userId);

	UserVO add(final UserVO vo);

	void remove(final Long id);
}
