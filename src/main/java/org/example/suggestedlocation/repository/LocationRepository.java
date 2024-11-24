package org.example.suggestedlocation.repository;

import org.example.suggestedlocation.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, BigInteger>, JpaSpecificationExecutor<Location> {
}
