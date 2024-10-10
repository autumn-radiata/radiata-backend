package radiata.service.timesale.core.implementation;

import com.github.ksuid.Ksuid;
import radiata.common.annotation.Implementation;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleProductIdCreator;

@Implementation
public class TimeSaleProductIdCreatorImpl implements TimeSaleProductIdCreator {

    @Override
    public String create() {
        return Ksuid.newKsuid().toString();
    }
}
