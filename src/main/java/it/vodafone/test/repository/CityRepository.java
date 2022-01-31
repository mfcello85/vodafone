package it.vodafone.test.repository;

import it.vodafone.test.entity.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {

    Optional<City> findByCode(String code);

    Optional<City> findByName(String name);

}
