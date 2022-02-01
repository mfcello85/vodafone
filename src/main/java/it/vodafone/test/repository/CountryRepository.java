package it.vodafone.test.repository;

import it.vodafone.test.entity.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

    Optional<Country> findByCode(String code);

    Optional<Country> findByName(String name);

}
