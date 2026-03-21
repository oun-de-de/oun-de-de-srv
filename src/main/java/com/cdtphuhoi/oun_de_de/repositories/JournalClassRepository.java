package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.JournalClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface JournalClassRepository extends JpaRepository<JournalClass, String> {
    List<JournalClass> findAllByIdIn(Collection<String> ids);
}
