
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {
    private static final String SERVER_ADDR = "localhost";
    private static final int SERVER_PORT = 8189;

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static final Scanner clientScanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
         try {
             socket = new Socket(SERVER_ADDR, SERVER_PORT);
             in = new DataInputStream(socket.getInputStream());
             out = new DataOutputStream(socket.getOutputStream());
             System.out.println("Клиент подсоединился к серверу. Можете начинать диалог:");
             //Тред получатель
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String strFromServer = in.readUTF();
                        if (strFromServer.equalsIgnoreCase("/end")) {
                            closeConnection();
                            break;
                        }
                        System.out.println("Сообщение сервера: " + strFromServer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //Тред отправщик
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        String strToServer = clientScanner.nextLine();
                        if (strToServer.equalsIgnoreCase("/end")) {
                            out.writeUTF("Клиент прекратил работу. Введите любое сообщение для завершения работы сервера.");
                            out.writeUTF(strToServer);
                            closeConnection();
                            break;
                        }
                        out.writeUTF(strToServer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
         } catch (IOException e ) {
             e.printStackTrace();
         }
    }

    public static void  closeConnection() throws IOException{
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
