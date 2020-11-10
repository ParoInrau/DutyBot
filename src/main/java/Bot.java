import org.apache.commons.math3.util.Precision;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Регистрация бота
 */
public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
        telegramBotsApi.registerBot(new Bot());

        } catch(TelegramApiRequestException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message отправка сообщения клиенту сервером
     * @param text текст сообщения
     */
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param sum строка, переданная клиентом серверу в сообщении
     * @return результат поиска по БД
     * @throws SQLException
     */

    public ResultSet searchCourt (String sum) throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Court court = new Court();
        court.setIdCourt(sum);
        ResultSet result = dbHandler.getCourt(court);
        return result;


    }

    /**
     *
     * @param update обновление данных, переданных клиентом
     *               Создается клавиатура под строкой ввода с двумя кнопками: налог и суд
     *               Предлагается нажать на одну из кнопок, в результате чего выводится соответствующее сообщение
     */
    public void onUpdateReceived(Update update) {
        double duty = 0;

        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch (message.getText()){
                case "/duty":
                    sendMsg(message, "Введите сумму судебного приказа:");
                            break;
                case "/court":
                    sendMsg(message, "Выберите суд. 1: Свердловский, 2: Ленинский, 3: Субъекта федерации");
                    break;
                default:

            }

                Message sum = update.getMessage(); //записывается текст сообщения
        double d = Double.parseDouble(String.valueOf(sum.getText())); //текст проверяется на "число- не число"
        String dStr = String.valueOf(sum.getText()); //вводится для распознавания команд о судах
        if (d <= 0){ //вывод сообщения о неверном значении числа
            sendMsg(message, "Вы ввели отрицательное число");

        }else if (dStr.equals("1")){ //вывод данных о суде по команде 1
            try {
                Court res = (Court)searchCourt(dStr);
                sendMsg(message, res.getAdressCourt());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if (dStr.equals("2")){ //вывод данных о суде по команде 2
            try {
                Court res = (Court)searchCourt(dStr);
                sendMsg(message, res.getAdressCourt());

            } catch (SQLException e) {
                e.printStackTrace();
            }}else if (dStr.equals("3")){ //вывод данных о суде по команде 3
            try {
                Court res = (Court)searchCourt(dStr);
                sendMsg(message, res.getAdressCourt());

            } catch (SQLException e) {
                e.printStackTrace();
            }}

        //в иных случаях, если вводится число, производится расчет пошлины по формуле в НК
        else if(d <= 20000){
            duty = (d*0.04)/2.0;
            if(duty<=200) duty = 200;
        }

        else if((d >20000) && (d <= 100000)){
            duty = (800 + (d-20000)*0.03)/2.0;
        }
        else if((d>100000) && (d<=200000)){
            duty = (3200 + (d-100000)*0.02)/2.0;
        }
        else if((d>200000) && (d<=1000000)){
            duty = (5200 + (d-200000)*0.01)/2.0;
        } else {
            duty = (13200 + (d-1000000)*0.005)/2.0;
            if(duty>60000) duty = 60000;
        }

        duty = Precision.round(duty, 2); //округление до 2 знаков после запятой (копейки)
        String dutyStr = String.valueOf(duty); //получение значения после округления, запись в строковую переменную
        sendMsg(message, "Государственная пошлина за подачу судебного приказа составляет: " + dutyStr + " рублей");



    }

    }

    /**
     *
     * @param sendMessage Отправка сообщения пользователем серверу, кнопки
     */

    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/duty")); //создание клавиатуры
        keyboardFirstRow.add(new KeyboardButton("/court"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


    }

    /**
     *
     * @return имя бота
     */
    public String getBotUsername() {
        return "court_duty_bot";
    }

    /**
     *
     * @return токен бота
     */
    public String getBotToken() {
        return "1084951476:AAE4dJBBZPf_663J3fCD_Ra2NpWlQRn6JLM";
    }
}
