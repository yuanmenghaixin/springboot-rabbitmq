package char4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Tim on 2019/4/30.
 * 配置的参数要和RabbitMQ管理界面配置的一样，否则报错
 */
public class RabbitProducer_AckConfirm {
    private static final String EXCHANGE_NAME = "exchange_normal";
    private static final String ROUTING_KEY = "key_normal";
    private static final String QUEUE_NAME = "queue_normal";
    private static final String EXCHANGE_NAME_DL = "exchange_dl";
    private static final String ROUTING_KEY_DL = "key_dl";
    private static final String QUEUE_NAME_DL = "queue_dl";
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
        channel.exchangeDeclare(EXCHANGE_NAME_DL, "direct", true, false, null);

        //创建一个持久化、非排他的、非自动删除的队列
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", "exchange_dl");
        arguments.put("x-dead-letter-routing-key", "key_dl");
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
        //将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        channel.queueDeclare(QUEUE_NAME_DL, true, false, false, null);
        //将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME_DL, EXCHANGE_NAME_DL, ROUTING_KEY_DL);
        //发送一条持久化的消息: hello world !
        String message = "Hello World !";
        channel.confirmSelect();//用于将当前的信道设置为publisher confirm模式
       /* channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Nack, SeqNo : " + deliveryTag + ", multiple : " + multiple);
                if (multiple) {
                    confirmSet.headSet(deliveryTag - 1).c l ear ();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    confirmSet.headSet(deliveryTag - 1).clear();
                }
                e1se {
                    confirmSet.remove(deliveryTag);
                }
            }
//注意这里需要添加处理消息重发的场景
//下面是演示一直发送消息的场景
        while(true)

            {
                long nextSeqNo = channel.getNextPublishSeqNo();
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                confirmSet.add(nextSeqNo);
            }

            //关闭资源
            //Thread.sleep(5000);
            channel.close();
            connection.close();
        }*/
    }
    }
