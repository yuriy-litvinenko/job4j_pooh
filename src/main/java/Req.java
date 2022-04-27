public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String lineSeparator = System.lineSeparator();
        int index1 = content.indexOf(' ');
        String httpRequestType = content.substring(0, index1);
        int index2 = content.indexOf(' ', index1 + 1);
        String url = content.substring(index1 + 2, index2);
        int index3 = url.indexOf('/');
        String poohMode = url.substring(0, index3);
        int index4 = url.indexOf('/', index3 + 1);
        String sourceName = index4 != -1 ? url.substring(index3 + 1, index4) : url.substring(index3 + 1);
        String param = "";
        if ("POST".equals(httpRequestType)) {
            int index5 = content.indexOf("x-www-form-urlencoded");
            int index6 = content.indexOf(lineSeparator, index5);
            param = content.substring(index6).trim();
        } else if ("GET".equals(httpRequestType)) {
            if (index4 != -1) {
                param = url.substring(index4 + 1);
            }
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
