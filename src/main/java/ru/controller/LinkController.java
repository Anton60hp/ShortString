package ru.controller;

import ru.Config;
import ru.entity.Link;
import ru.service.LinkService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    public void createField() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите URL: ");
        String longUrl = scanner.nextLine();

        System.out.println("Введите пользователя: ");
        String userId = scanner.nextLine();
        Optional<Integer> uses = Optional.empty();
        Optional<Integer> timeToLive = Optional.empty();

        System.out.println("Введите колличество использований для ссылки: ");
        while (true) {
            if (scanner.hasNextInt()) {
                uses = Optional.of(scanner.nextInt());
                break;
            } else if (!scanner.hasNext()) break;
            else {
                System.out.println("Введите целочисленное значение.");
                scanner.next();
            }
        }

        System.out.println("Введите время жизни ссылки в минутах: ");
        while (true) {
            if (scanner.hasNextInt()) {
                timeToLive = Optional.of(scanner.nextInt());
                break;
            } else if (!scanner.hasNext()) break;
            else {
                System.out.println("Введите целочисленное значение.");
                scanner.next();
            }
        }

        try {
            Link link = linkService.createLink(longUrl, UUID.nameUUIDFromBytes(userId.getBytes()), uses.orElse(Config.DEFAULT_USES), timeToLive.orElse(Config.DEFAULT_EXPIRATION_TIME));
            System.out.println("Ваша ссылка:");
//            System.out.println(link);
            System.out.println(link.getShortLink());
            System.out.println("Количество использований: " + link.getUsesLeft());
            System.out.println("Валидна до: " + link.getExpirationDate());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resolveLink(String s) {
        try {
            linkService.redirectToLongLink(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Link> checkLink() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите короткую ссылку: ");
        String shortUrl = scanner.nextLine();

        System.out.println("Введите пользователя: ");
        String userId = scanner.nextLine();

        Optional<Link> linkOpt = linkService.checkLink(shortUrl, UUID.nameUUIDFromBytes(userId.getBytes()));
        if (linkOpt.isPresent()) {
            Link link = linkOpt.get();
            System.out.println("Ссылка найдена.");
            System.out.println("Ваша ссылка:");
//            System.out.println(link);
            System.out.println(link.getShortLink());
            System.out.println("Количество использований: " + link.getUsesLeft());
            System.out.println("Валидна до: " + link.getExpirationDate());
            System.out.println("---------");
            return linkOpt;
        } else {
            System.out.println("Созданная вами ссылка с таким сокращённым URL не найдена");
            return Optional.empty();
        }
    }


    public void editLink(Optional<Link> linkOptional) {
        if (linkOptional.isPresent()) {
            Link link = linkOptional.get();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Что вы хотите изменить?");
                System.out.println();
                System.out.println("'uses' - для изменения оставшегося кол-ва использований");
                System.out.println("'time' - для изменения оставшегося времени доступности ссылки");
                System.out.println("'exit' - для выхода из редактирования");


                String s = scanner.nextLine();

                if (s.equals("exit")) break;
                else if (s.equals("uses")) {
                    System.out.println("Введите новое количество возможных использований");

                    if (scanner.hasNextInt()) {
                        link.setUsesLeft(scanner.nextInt());
                        linkService.saveLink(link);
                    } else System.out.println("Введите целочисленное значение.");

                } else if (s.equals("time")) {
                    System.out.println("Введите новое время действия в минутах");


                    if (scanner.hasNextInt()) {
                        link.setExpirationDate(LocalDateTime.now().plusMinutes(scanner.nextInt()));
                        linkService.saveLink(link);
                        break;
                    } else System.out.println("Введите целочисленное значение.");


                } else System.out.println("Некорректный ввод.");

            }
        }
    }

    public void deleteLink(Optional<Link> link) {
        link.ifPresent(linkService::deleteLink);
    }
}
