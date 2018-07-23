package br.com.nextel.shorturl.service;

import br.com.nextel.shorturl.domain.entity.UserEntity;
import br.com.nextel.shorturl.domain.vo.UserVO;
import br.com.nextel.shorturl.exception.BusinessException;

public interface UserService {
	boolean isExistingUser(Long userId); 

	UserEntity add(final UserVO vo) throws BusinessException;

	void remove(final Long id);
}
