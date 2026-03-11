package de.casino.banking_service.user.repository;

import de.casino.banking_service.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> { //SpringData erzeugt methoden für db Zugriff, generisches repository<entity, type of primärschlüssel
    // speichern UserEntity, ID ist Long
}
