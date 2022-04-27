import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenTopic2() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "milk=18";
        String paramForPublisher2 = "bread=20";
        String paramForSubscriber1 = "client1";
        String paramForSubscriber2 = "client2";
        /* Подписываемся получателем client1 на топик shop */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "shop", paramForSubscriber1)
        );

        /* Добавляем значение в топик shop */
        topicService.process(
                new Req("POST", "topic", "shop", paramForPublisher)
        );

        /* Инициируем получение данных из топика shop получателями client1 и client2.
        Значение получит только подписанный на топик client1, для client2 произойдет подписание на топик */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "shop", paramForSubscriber1)
        );
        Resp result3 = topicService.process(
                new Req("GET", "topic", "shop", paramForSubscriber2)
        );

        /* Добавляем еще одно значение в топик shop */
        topicService.process(
                new Req("POST", "topic", "shop", paramForPublisher2)
        );

        /* Теперь client2 также получит значение из топика */
        Resp result4 = topicService.process(
                new Req("GET", "topic", "shop", paramForSubscriber2)
        );

        assertThat(result1.text(), is(""));
        assertThat(result1.status(), is("204"));
        assertThat(result2.text(), is("milk=18"));
        assertThat(result2.status(), is("200"));
        assertThat(result3.text(), is(""));
        assertThat(result3.status(), is("204"));
        assertThat(result4.text(), is("bread=20"));
        assertThat(result4.status(), is("200"));
    }
}
