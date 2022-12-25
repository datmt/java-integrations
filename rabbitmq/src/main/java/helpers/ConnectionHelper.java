package helpers;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionHelper {

   public static Connection createConnection() throws IOException, TimeoutException {

       ConnectionFactory factory = new ConnectionFactory();
       factory.setHost("localhost");
       factory.setPort(5672);
       // create a connection
       return factory.newConnection();
   }
}
