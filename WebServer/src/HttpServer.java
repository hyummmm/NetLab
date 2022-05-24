import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable{
    public static final int port = 8080; // 默认监控端口
    public static final String WEB_Root = System.getProperty("user.dir") + File.separator + "webroot";
    private static final String SHUTDOWN_COMMAND="/SHUTDOWN";
    private boolean shutdown=false;


    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        Thread thread = new Thread(server);
        thread.start();
    }

    // 多线程
    public void run() {
        try {
            await();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @SuppressWarnings("resource")
    public void await() {
        ServerSocket serverSocket = null;   // 声明ServerSocket实例
        try{
            // 创建一个ServerSocket实例，然后进入while循环等待来自客户端的请求。
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1")); // 监听地址
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        while(!shutdown) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;

            try {
                socket = serverSocket.accept(); // 打开一个套接字，等待直到8080端口收到一个HTTP请求
                // 从accept返回的Socket获得一个java.io.InputStream和一个java.io.OutputStream
                input = socket.getInputStream();
                output = socket.getOutputStream();
                // 创建一个Request对象，调用parse方法解析原始的HTTP请求
                Request request = new Request(input);
                request.parse();
                // 创建一个Response对象，把前面创建的Request对象传递给它，调用它的sendRessource方法
                Response response = new Response(output,request);
                response.sendResource();
                socket.close();
                // 检查该URI是否为关闭服务器的命令
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

    }
}
