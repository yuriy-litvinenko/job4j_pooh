import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp;
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).add(req.getParam());
            resp = new Resp("", "501");
        } else if ("GET".equals(req.httpRequestType())) {
            var text = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
            resp = text != null ? new Resp(text, "200") : new Resp("", "204");
        } else {
            resp = new Resp("", "501");
        }
        return resp;
    }
}
