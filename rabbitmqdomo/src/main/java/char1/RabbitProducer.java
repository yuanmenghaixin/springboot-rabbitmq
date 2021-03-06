package char1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * exchange,channel和queue配置的参数要和RabbitMQ管理界面配置的一样，否则报错
 */
public class RabbitProducer {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672;//RabbitMQ 服务端默认端口号为5672

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //TODO 版本不一致，记得关闭Activemq
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();//创建连接
        Channel channel = connection.createChannel();//创建信道
        // 创建一个type="direct" 、持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        //创建一个持久化、非排他的、非自动删除的队列
        Map<String, Object> arguments = new HashMap<String, Object>();
        //arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", "exchange_dl");
        //arguments.put("x-dead-letter-routing-key", " key_dl");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        //发送一条持久化的消息: hello world !
        String message = "Hello World !";
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.deliveryMode(2); // 持久化消息
        builder.expiration("10000");//设置TTL=60000ms
        AMQP.BasicProperties properties = builder.build();
        //channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, properties, message.getBytes());
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("B asic.Return 返回的结果是: " + message);
            }
        });
        //关闭资源
        //Thread.sleep(5000);
        channel.close();
        connection.close();
    }
}
