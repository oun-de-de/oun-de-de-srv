package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.common.OrgManaged;
import java.util.Optional;

public interface BaseRepository<T extends OrgManaged> {
    Optional<T> findOneById(String id);
}
