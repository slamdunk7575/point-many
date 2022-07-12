package com.jun.pointmany.dao;

import com.jun.pointmany.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {
    Place findByPlaceId(String placeId);
}
