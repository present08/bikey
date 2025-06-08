package com.bikey.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bikey.server.model.BikeyProduct;

public interface ProductRepository extends JpaRepository<BikeyProduct, Long> {
    @Query("SELECT p.productName FROM BikeyProduct p where p.division = :param")
    List<String> findByProductName(@Param("param") String param);

    @Query("SELECT p.transName FROM BikeyProduct p where p.division = :param")
    List<String> findByProductTransName(@Param("param") String param);

    @Query("SELECT p.productName FROM BikeyProduct p where p.division IN :param")
    List<String> findByListProductName(@Param("param") List<String> param);

    @Query("SELECT p.transName FROM BikeyProduct p where p.division IN :param")
    List<String> findByListProductTranName(@Param("param") List<String> param);

    @Query("SELECT p.productName FROM BikeyProduct p")
    List<String> findAllByProductName();

    @Query("SELECT p.transName FROM BikeyProduct p")
    List<String> findAllByTransName();
}
