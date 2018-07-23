package br.com.nextel.shorturl.service.impl;

import br.com.nextel.shorturl.domain.entity.UserEntity;
import br.com.nextel.shorturl.domain.repository.UserRepository;
import br.com.nextel.shorturl.domain.vo.UserVO;
import br.com.nextel.shorturl.exception.BusinessException;
import br.com.nextel.shorturl.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isExistingUser(final Long userId) {
		Optional<UserEntity> user = userRepository.findById(userId);
		if(user.isPresent())
			return true;

		return false;
	}

	@Override
	public UserEntity add(final UserVO vo) throws BusinessException {
		Optional<UserEntity> user = userRepository.findByName(vo.getName());
		if (user.isPresent()) {
		    throw new BusinessException("Usuário com nome '" + vo.getName() + "' já existe");
        }
        return userRepository.save(vo.toEntity());
	}

	@Override
	public void remove(final Long id) {
		try {
			userRepository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			logger.info("User not found");
		}
	}
}