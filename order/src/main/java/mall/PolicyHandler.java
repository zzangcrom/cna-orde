package mall;

import mall.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler
{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString) {

    }

    @Autowired
    OrderRepository orderRepository;  //이건 pre loading  lazy loading?


    @StreamListener(KafkaProcessor.INPUT)   // kafka가 읽어와서
    public void wheneverShipped_UpdateStatus(@Payload Shipped shipped)
    {
        if (shipped.isMe())
        {
            System.out.println("##### listener UpdateStatus : " + shipped.toJson());
            // 재고량 수정
            Optional<Order> orderOptional = orderRepository.findById(shipped.getOrderId());
            Order order = orderOptional.get();
            order.setStatus(shipped.getStatus());

            orderRepository.save(order);  //이 경우에는 POSTPersist 가 아닌 업데이트 이벤트가 발생한다...오더 어그리게이트에서 보자
        }

    }
}
