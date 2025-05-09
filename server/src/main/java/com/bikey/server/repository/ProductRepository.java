package com.bikey.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bikey.server.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT product_name FROM Product where division = :param")
    List<String> findByProductName(@Param("param") String param);

    @Query("SELECT trans_name FROM Product where division = :param")
    List<String> findByProductTransName(@Param("param") String param);

    @Query("SELECT product_name FROM Product where division IN :param")
    List<String> findByListProductName(@Param("param") List<String> param);

    @Query("SELECT trans_name FROM Product where division IN :param")
    List<String> findByListProductTranName(@Param("param") List<String> param);
}
