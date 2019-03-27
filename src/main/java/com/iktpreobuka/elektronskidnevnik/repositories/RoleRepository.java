package com.iktpreobuka.elektronskidnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskidnevnik.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

	RoleEntity findByName(String string);
}
