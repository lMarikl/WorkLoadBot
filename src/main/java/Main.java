import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            String result = "";
            for (int i = 0; i < employees.size(); i++) {
                result += employees.get(i) + " ---> " + new MyRequest().getWorkLoad(employees.get(i)) + "\n";
            }

            SendMessage message = new SendMessage()
                    .setChatId(chat_id)
                    .setText(result);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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