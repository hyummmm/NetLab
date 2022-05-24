import java.io.IOException;
import java.io.InputStream;

/*
Request请求：方法-URI-协议/版本，请求头，请求正文
GET /servlet/default.jsp HTTP/1.1
Accept: text/plain; text/html
Accept-Language: en-gb
Connection: Keep-Alive
Host: localhost
Referer: http://localhost/ch8/SendDetails.htm
User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
Content-Length: 33
Content-Type: application/x-www-form-urlencoded
Accept-Encoding: gzip, deflate

userName=JavaJava&userID=javaID
 */
public class Request {
    private InputStream input;
    private String uri = null;

    // 创建Request类的实例时要传入一个从负责与客户端通信的Socket获得的InputStream对象
    public Request(InputStream input) {
        this.input = input;
    }

    // 解析HTTP请求中的原始数据，提取url
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];

        try {
            i = input.read(buffer); // 调用InputStream对象的其中一个read方法可获得HTTP请求的原始数据
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }

        for (int j=0; j<i; j++)
        {
            request.append((char) buffer[j]);
        }
        System.out.println("*********Http-request*********");
        System.out.print(request.toString());
        uri = parseUri(request.toString());
    }

    // 返回HTTP请求的URI
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        //POST /default.jsp HTTP/1.1
        //GET /index.html HTTP/1.1
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1); //从第一个空格开始，寻找到最后一个空格为止
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2); //返回等同一个文件目录
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}
