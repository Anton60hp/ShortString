package ru.impl;

import ru.entity.Link;
import ru.repository.LinkRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LinkRepositoryImpl implements LinkRepository {

    private final Map<String, Link> links = new HashMap<>();

    @Override
    public Optional<Link> save(Link link) {
        links.put(link.getShortLink(), link);
        return Optional.of(link);
    }


    @Override
    public Optional<Link> findByShortLink(String shortLink) {
        return Optional.ofNullable(links.get(shortLink));
    }


    @Override
    public Map<String, Link> findAll() {
        return links;
    }

    @Override
    public void delete(Link link) {
        links.remove(link.getShortLink());
    }
}
