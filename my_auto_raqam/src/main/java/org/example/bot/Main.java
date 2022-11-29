package org.example.bot;

import org.example.jdbc_database.AuksionDataBase;
import org.example.jdbc_database.AvtoNumberDataBase;
import org.example.jdbc_database.UserDatabase;
import org.example.model.Auksion;
import org.example.model.AutoNumber;
import org.example.model.Base;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends TelegramLongPollingBot implements BaseServiceBot, BotConstants, regions {
    static UserDatabase userDatabase = new UserDatabase();
    static AuksionDataBase auksionDataBase = new AuksionDataBase();
    static AvtoNumberDataBase avtoNumberDataBase = new AvtoNumberDataBase();
    static Map<Long, Integer> AUTO_NUMBER_MESSAGE_MAP = new ConcurrentHashMap<>();
    static Map<Long, Integer> AUKSION_INFO_MESSAGE_MAP = new ConcurrentHashMap<>();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                Long chatId = message.getChatId();
                if (text.equals("/start")) {
                    myExecute("WELCOME TO AUTO NUMBER AUKSION BOT", chatId,
                            generateReplyKeyboard(List.of(SETTINGS, NUMBERS)), null);
                } else if (text.equals(NUMBERS)) {
                    Integer pageIndex = AUKSION_INFO_MESSAGE_MAP.getOrDefault(chatId, DEFAULT_PAGE_INDEX);
                    InlineKeyboardMarkup buttons = getInlineKeyboardAutoNumberList(pageIndex);
                    Integer messageId = myExecute(AUTO_NUMBERS, chatId, null, buttons);
                    AUTO_NUMBER_MESSAGE_MAP.put(chatId, messageId);
                } else if (text.endsWith("SUM") || text.endsWith("sum")){
                    //auksiondatabase
                }
            } else if (message.hasContact()) {
                deleteFirstNumberInlineKeyboard(message.getContact());
                repeatFirstNumberInlineKeyboard(message.getContact());
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            CallbackQuery callbackQuery = update.getCallbackQuery();

            if (!userDatabase.checkUser(chatId)) {
                ReplyKeyboardMarkup r = shareContact();
                myExecute("PLEASE SHARE YOUR CONTACT", callbackQuery.getMessage().getChatId(), r, null);
            }
            if (isExistPage(data)) {
                String[] split = data.split(SPLIT_REGAX);
                int index = Integer.parseInt(split[2]);
                AUKSION_INFO_MESSAGE_MAP.put(chatId, index);

                if (split[1].equals(CategoryState.AUTONUMBER.name())) {
                    InlineKeyboardMarkup buttons = getInlineKeyboardAutoNumberList(index);
                    Integer prevMessageId = AUTO_NUMBER_MESSAGE_MAP.get(chatId);
                    Integer messageId = myExecuteEdit(AUTO_NUMBERS, chatId, prevMessageId, buttons);
                    AUTO_NUMBER_MESSAGE_MAP.put(chatId, messageId);
                }

            }
            if (data.startsWith(CategoryState.AUTONUMBER.name())) {
                String text = getAuksion(data);
                InlineKeyboardMarkup i = generateInlineKeyboard(List.of(RAISE_PRICE), isAutoNumberFalse, isRegionFalse);
                Integer oldPageIndex = myExecuteEdit(text, chatId, AUTO_NUMBER_MESSAGE_MAP.get(chatId), i);
                AUTO_NUMBER_MESSAGE_MAP.put(chatId, oldPageIndex);
            }
            if (data.equals(BACK)) {
                Integer index = AUKSION_INFO_MESSAGE_MAP.getOrDefault(chatId, DEFAULT_PAGE_INDEX);
                InlineKeyboardMarkup buttons = getInlineKeyboardAutoNumberList(index);
                Integer prevMessageId = AUTO_NUMBER_MESSAGE_MAP.get(chatId);
                Integer messageId = myExecuteEdit(AUTO_NUMBERS, chatId, prevMessageId, buttons);
                AUTO_NUMBER_MESSAGE_MAP.put(chatId, messageId);
            }
            if (data.equals(RAISE_PRICE)) {
                Integer messageId = myExecute("ENTER PRICE (f.e -> 7777SUM)", chatId, null, null);
                AUTO_NUMBER_MESSAGE_MAP.put(chatId,messageId);
            }
            if (isSEARCH(data)) {
                InlineKeyboardMarkup buttons = generateInlineKeyboard(List.of(
                                TOSHKENT, TOSHKENT_VIL, QASHQADARYO,
                                QORAQALPOGISTON, SAMARQAND, SIRDARYO,
                                SURXONDARYO, ANDIJON, NAMANGAN,
                                JIZZAX, NAVOIY, BUXORO,
                                XORAZM, FARGONA
                        ), isAutoNumberFalse, isRegionTrue
                );
                Integer messageId = myExecuteEdit("CHOOSE REGION", chatId, AUTO_NUMBER_MESSAGE_MAP.get(chatId), buttons);
                AUTO_NUMBER_MESSAGE_MAP.put(chatId, messageId);
            }
            if (data.startsWith(isRegionTrue)) {
                String region = getRegion(data);
                List<String> autoNumbers = new ArrayList<>();
                for (AutoNumber autoNumber : avtoNumberDataBase.getAutoNumberListByRegionName(region)) {
                    autoNumbers.add(autoNumber.getNumber());
                }
                InlineKeyboardMarkup buttons = generateInlineKeyboard(autoNumbers, isAutoNumberTrue, isRegionFalse);
                Integer messageId = myExecuteEdit(region, chatId, AUTO_NUMBER_MESSAGE_MAP.get(chatId), buttons);
                AUTO_NUMBER_MESSAGE_MAP.put(chatId, messageId);
            }
        }
    }


    private static String getRegion(String data) {
        String[] split = data.split(SPLIT_REGAX);
        return split[1];
    }

    private static boolean isSEARCH(String data) {
        String[] split = data.split(SPLIT_REGAX);
        return split[0].equals(SEARCH);
    }

    private static String preNumber(String data) {
        String[] split = data.split(SPLIT_REGAX);
        return split[1].substring(0, 2);
    }

    private static String postNumber(String data) {
        String[] split = data.split(SPLIT_REGAX);
        return split[1].substring(3);
    }

    private InlineKeyboardMarkup generateInlineKeyboard(List<String> menuList, String isAutoNumber, String isRegion) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(rows);
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (int i = 1; i <= menuList.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(menuList.get(i - 1));
            button.setCallbackData(isRegion + isAutoNumber + menuList.get(i - 1));
            buttons.add(button);
            if (i % DIVIDER == 0) {
                rows.add(buttons);
                buttons = new ArrayList<>();
            }
        }
        if (!buttons.isEmpty()) {
            rows.add(buttons);
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(BACK);
        button.setCallbackData(BACK);
        rows.add(List.of(button));
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getInlineKeyboardAutoNumberList(int pageIndex) {
        return new MainServiceBot(new AvtoNumberDataBase()).getButtons(DIVIDER, pageIndex);
    }

    private ReplyKeyboardMarkup shareContact() {
        ReplyKeyboardMarkup r = new ReplyKeyboardMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton(SHARE_CONTACT);
        keyboardButton.setRequestContact(true);
        keyboardRow.add(keyboardButton);
        r.setKeyboard(List.of(
                keyboardRow
        ));
        r.setResizeKeyboard(true);
        r.setSelective(true);
        r.setOneTimeKeyboard(true);
        return r;
    }

    private Integer myExecute(String text, long chatId, ReplyKeyboardMarkup r, InlineKeyboardMarkup i) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(r == null ? i : r);
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            return execute(sendMessage).getMessageId();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private Integer myExecuteEdit(String text, long chatId, Integer prevMessageId, InlineKeyboardMarkup i) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(prevMessageId);
        editMessageText.setReplyMarkup(i);
        editMessageText.setText(text);
        editMessageText.enableMarkdown(true);
        try {
            execute(editMessageText);
            return editMessageText.getMessageId();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getAuksion(String data) {
        String preNumber = preNumber(data);
        String postNumber = postNumber(data);
        Auksion auksion = auksionDataBase.getAuksion(preNumber, postNumber);
        String text =
                "*AUKSION ID:* " + auksion.getId() + "\n" +
                        "*AUTO NUMBER:* " + auksion.getPreNumber() + " " + auksion.getPostNumber() + "\n" +
                        "*STATE:* " + auksion.getState() + "\n" +
                        "*START DATE:* " + auksion.getStartDate() + "\n" +
                        "*END DATE:* " + auksion.getEndDate() + "\n" +
                        "*ADDRESS REGISTRATION:* " + auksion.getName() + "\n" +
                        "*INITIAL PRICE:* " + auksion.getInitialPrice() + " SUM\n" +
                        "*CURRENT PRICE:* " + auksion.getCurrentPrice() + " SUM\n" +
                        "*SUGGESTED PRICE:* " + auksion.getSuggestedPrice() + " SUM";
        if (auksion.getUserId() != 0) {
            text += "\n" + "*USER ID:* " + auksion.getUserId();
        }
        return text;
    }
    private ReplyKeyboardMarkup generateReplyKeyboard(List<String> menuList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow keyboardButton = new KeyboardRow();
        for (int i = 1; i <= menuList.size(); i++) {
            keyboardButton.add(menuList.get(i - 1));
            if (i % DIVIDER == 0) {
                keyboardRowList.add(keyboardButton);
                keyboardButton = new KeyboardRow();
            }
        }
        if (!keyboardButton.isEmpty()) {
            keyboardRowList.add(keyboardButton);
        }
        return replyKeyboardMarkup;
    }

    private void deleteMessage(int deleteMessageId, long chatId) {
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), deleteMessageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void deleteFirstNumberInlineKeyboard(Contact contact) {
        Integer deleteMessageId = AUTO_NUMBER_MESSAGE_MAP.get(contact.getUserId());
        deleteMessage(deleteMessageId, contact.getUserId());
    }

    private void repeatFirstNumberInlineKeyboard(Contact contact) {
        InlineKeyboardMarkup buttons = getInlineKeyboardAutoNumberList(DEFAULT_PAGE_INDEX);
        Integer messageId = myExecute(AUTO_NUMBERS, contact.getUserId(), null, buttons);
        AUTO_NUMBER_MESSAGE_MAP.put(contact.getUserId(), messageId);
        userDatabase.addUser(contact.getUserId(), contact.getFirstName() + " " + contact.getLastName(), contact.getPhoneNumber(), contact.getPhoneNumber(), 1);
    }
}
