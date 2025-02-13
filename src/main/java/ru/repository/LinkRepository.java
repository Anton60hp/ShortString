package ru.repository;

import ru.entity.Link;

import java.util.Map;
import java.util.Optional;


public interface LinkRepository {

    Optional<Link> save(Link link);

    Optional<Link> findByShortLink(String shortLink);

    Map<String, Link> findAll();

    void delete(Link link);

}
