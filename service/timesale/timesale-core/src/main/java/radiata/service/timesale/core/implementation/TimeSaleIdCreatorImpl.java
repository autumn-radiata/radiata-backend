package radiata.service.timesale.core.implementation;

import com.github.ksuid.Ksuid;
import radiata.common.annotation.Implementation;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleIdCreator;

@Implementation
public class TimeSaleIdCreatorImpl implements TimeSaleIdCreator {

    @Override
    public String create() {

        return Ksuid.newKsuid().toString();
    }
}
