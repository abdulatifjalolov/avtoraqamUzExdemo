package org.example;

import org.example.jdbc_database.AuksionDataBase;
import org.example.jdbc_database.AvtoNumberDataBase;
import org.example.jdbc_database.UserDatabase;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

public class Main {
    static AvtoNumberDataBase avtoNumberDataBase = new AvtoNumberDataBase();

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new org.example.bot.Main());
        while (true) {
            System.out.println("1.ADD USER 2.ADD AUTO NUMBER 0.BACK");
            int var = new Scanner(System.in).nextInt();
            if (var == 0) {
                break;
            }
            switch (var) {
                case 1 -> {

                }
                case 2 -> {
                    System.out.println("ENTER POST_NUMBER:");
                    String postNumber = new Scanner(System.in).nextLine();
                    System.out.println("ENTER REGION_NUMBER:");
                    int regionNumber = new Scanner(System.in).nextInt();
                    System.out.println("ENTER STATE:");
                    int state = new Scanner(System.in).nextInt();
                    System.out.println(avtoNumberDataBase.addAutoNumber(postNumber, regionNumber, state));
                }
            }
        }
    }
}