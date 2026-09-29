package ca.hccis.softball.repositories;

import ca.hccis.softball.jpa.entity.CodeValue;
import ca.hccis.softball.jpa.entity.CodeValueId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeValueRepository extends CrudRepository<CodeValue, CodeValueId> {
}