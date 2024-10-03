package radiata.service.order.core.implemetation;

import com.github.ksuid.KsuidGenerator;
import radiata.common.annotation.Implementation;

@Implementation
public class OrderIdCreator {

    public String createOrderId() {
        return KsuidGenerator.generate();
    }
}
