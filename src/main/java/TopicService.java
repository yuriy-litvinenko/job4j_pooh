import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp;
        if ("POST".equals(req.httpRequestType())) {
            for (ConcurrentLinkedQueue<String> subscriber :
                    topics.getOrDefault(req.getSourceName(), new ConcurrentHashMap<>()).values()) {
                subscriber.add(req.getParam());
            }
            resp = new Resp("", "501");
        } else if ("GET".equals(req.httpRequestType())) {
            topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            var text = topics.get(req.getSourceName()).get(req.getParam()).poll();
            resp = text != null ? new Resp(text, "200") : new Resp("", "204");
        } else {
            resp = new Resp("", "501");
        }
        return resp;
    }
}
