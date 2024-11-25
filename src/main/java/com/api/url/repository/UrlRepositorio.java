package com.api.url.repository;

import com.api.url.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface UrlRepositorio extends JpaRepository<Url, String> {

}
