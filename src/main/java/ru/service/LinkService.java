package ru.service;

import ru.entity.Link;
import lombok.RequiredArgsConstructor;
import ru.repository.LinkRepository;
import ru.Config;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class LinkService {
    LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link createLink(String longLink, UUID userId, int uses, int timeToLive) {
        if (uses < Config.DEFAULT_USES) uses = Config.DEFAULT_USES;                                     // берём наибольшее из введённого и конфигов
        if (timeToLive > Config.DEFAULT_EXPIRATION_TIME) timeToLive = Config.DEFAULT_EXPIRATION_TIME;   // берём наименьшее из введённого и конфигов
        Link link = Link.builder()
                .longLink(longLink)
                .userUUID(userId)
                .usesLeft(uses)
                .shortLink(shortenLink(longLink, userId))
                .expirationDate(LocalDateTime.now().plusMinutes(timeToLive))
                .build();
        return linkRepository.save(link).orElseThrow();
    }

    private String shortenLink(String link, UUID user) {
        return "lnk.ru/" + String.format("%x", (link + ":" + user.toString() + LocalDateTime.now().getNano()).hashCode());
    }

    public void redirectToLongLink(String shortLink) {
        try {
            if (linkRepository.findByShortLink(shortLink).isPresent()) {
                Link link = linkRepository.findByShortLink(shortLink).get();
                if (link.getExpirationDate().isBefore(LocalDateTime.now())){
                    System.out.println("Время жизни ссылки истекло и она будет удалена");
                    deleteLink(link);
                }
                else if (link.getUsesLeft() <= 0) {
                    System.out.println("Ссылка была использована максимальное колличество раз и будет удалена");
                    deleteLink(link);
                } else {
                    link.setUsesLeft(link.getUsesLeft() - 1);
                    linkRepository.save(link);
                    Desktop.getDesktop().browse(new URI(link.getLongLink()));
                }
            } else System.out.println("Ссылка не найдена");
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Link> checkLink(String shortLink, UUID userUUID) {
        Optional<Link> link = linkRepository.findByShortLink(shortLink);
        if (link.isPresent()) {
            if (link.get().getUserUUID().equals(userUUID)) {
                return link;
            }
        }
        return Optional.empty();
    }

    public void deleteLink(Link link) {
        linkRepository.delete(link);
    }

    public void saveLink(Link link) {
        linkRepository.save(link);
    }
}
