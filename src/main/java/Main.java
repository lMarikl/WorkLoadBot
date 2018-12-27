import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Main extends TelegramLongPollingBot {
    private static ArrayList<String> employees = new ArrayList<String>();

    public static void main(String[] args) {
        employees.add("Сергей Кремлев");
        employees.add("Андрей Федориненко");
        employees.add("Вадим Черный");
        employees.add("Алексей Мешков");
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new Main());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            StringBuilder result = new StringBuilder();
            for (String employee : employees) {
                result.append(employee).append(" ---> ").append(new MyRequest().getWorkLoad(employee)).append("\n");
            }

            String s = message.getText();
            if ("/start".equals(s)) {
                sendMsg(message, "Привет, я считаю загрузку всего региона");

            } else if ("За сегодня".equals(s)) {
                sendMsg(message, result.toString());

            } else if ("За месяц".equals(s)) {
                sendMsg(message, "Так я еще не умею ;)");

            } else {
                sendMsg(message, ".i.");
            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("За сегодня");
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("За месяц");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "WorkLoadBot";
    }

    @Override
    public String getBotToken() {
        return "753950806:AAHfu2W139_pT45ajhyg_YqD5ftXW5GyJUU";
    }
}