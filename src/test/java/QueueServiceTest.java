import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenPostThenGetQueue2() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "milk=18";
        String paramForPostMethod2 = "bread=20";
        /* Пробуем забрать данные из несозданной очереди shop. Получаем пустое значение */
        Resp result = queueService.process(
                new Req("GET", "queue", "shop", null)
        );
        /* Добавляем данные в очередь shop */
        queueService.process(
                new Req("POST", "queue", "shop", paramForPostMethod)
        );
        queueService.process(
                new Req("POST", "queue", "shop", paramForPostMethod2)
        );
        /* Забираем данные из очереди weather */
        Resp result2 = queueService.process(
                new Req("GET", "queue", "shop", null)
        );
        Resp result3 = queueService.process(
                new Req("GET", "queue", "shop", null)
        );
        Resp result4 = queueService.process(
                new Req("GET", "queue", "shop", null)
        );
        assertThat(result.text(), is(""));
        assertThat(result.status(), is("204"));
        assertThat(result2.text(), is("milk=18"));
        assertThat(result2.status(), is("200"));
        assertThat(result3.text(), is("bread=20"));
        assertThat(result3.status(), is("200"));
        assertThat(result4.text(), is(""));
        assertThat(result4.status(), is("204"));
    }
}
