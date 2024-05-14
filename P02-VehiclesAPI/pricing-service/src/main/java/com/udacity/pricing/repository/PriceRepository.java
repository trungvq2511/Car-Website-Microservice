package com.udacity.pricing.repository;

import com.udacity.pricing.entity.Price;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface PriceRepository extends JpaRepository<Price, Long>{

}
