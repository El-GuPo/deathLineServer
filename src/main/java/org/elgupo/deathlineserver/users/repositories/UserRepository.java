package org.elgupo.deathlineserver.users.repositories;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    UserEntity findUserById(Long id);
}
