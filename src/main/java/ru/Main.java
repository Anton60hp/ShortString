package ru;


import ru.controller.LinkController;
import ru.impl.LinkRepositoryImpl;
import ru.service.LinkService;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        LinkRepositoryImpl linkRepository = new LinkRepositoryImpl();
        LinkService linkService = new LinkService(linkRepository);
        LinkController linkController = new LinkController(linkService);


        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("'new' - для генерации новой ссылки");
            System.out.println("'list' - для вывода всех ссылок");
            System.out.println("'edit' - для изменения созданной ссылки");
            System.out.println("'delete' - для удаления созданной ссылки");
            System.out.println("'exit' - для выхода");

            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            else if (s.equals("new")) {
                System.out.println("Создание новой ссылки");
                linkController.createField();
            } else if (s.equals("list")) {
                System.out.println(linkRepository.findAll().values());
            } else if (s.equals("edit")) {
                linkController.editLink(linkController.checkLink());
            } else if (s.equals("delete")) {
                linkController.deleteLink(linkController.checkLink());
            } else if (s.startsWith("lnk.ru/")) {
                linkController.resolveLink(s);
            } else {
                System.out.println("Некорректный ввод.");
            }
        }
    }
}