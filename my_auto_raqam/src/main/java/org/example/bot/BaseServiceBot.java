package org.example.bot;

import org.example.jdbc_database.BaseDatabase;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface BaseServiceBot extends BotConstants {
    default InlineKeyboardMarkup getButtons(int divider, int pageIndex) {
        return null;
    }

    default boolean isExistPage(String callBackDate) {
        String[] split = callBackDate.split(SPLIT_REGAX);
        return (split[0].startsWith(PREV) || split[0].startsWith(NEXT)) && !split[2].equals(FIRST_PAGE);
    }
}
