import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        Scanner serverScanner = new Scanner(System.in);
        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер работает, ожидает подключение клиента");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился.");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Тред получатель
            Socket finalSocket = socket;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String strFromClient = in.readUTF();
                            if (strFromClient.equalsIgnoreCase("/end")) {
                                try {
                                    in.close();
                                    out.close();
                                    finalSocket.close();
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            System.out.println("Сообщение клиента: " + strFromClient);
                    }
                } catch (IOException e ) {
                        e.printStackTrace();
                    }
            }
            }).start();
            //Тред отправитель
            Socket finalSocket1 = socket;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String strToClient = serverScanner.nextLine();
                            if (strToClient.equalsIgnoreCase("/end")) {
                                out.writeUTF("Сервер прекратил работу. Введите любое сообщение для завершения работы клиента.");
                                out.writeUTF(strToClient);
                                try {
                                    in.close();
                                    out.close();
                                    finalSocket1.close();
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            out.writeUTF(strToClient);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }

