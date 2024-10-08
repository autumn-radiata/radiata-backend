package radiata.service.coupon.core.implementation;

import com.github.ksuid.Ksuid;
import radiata.common.annotation.Implementation;
import radiata.service.coupon.core.implementation.interfaces.CouponIdCreator;

@Implementation
public class CouponIdCreatorImpl implements CouponIdCreator {

    @Override
    public String create() {

        return Ksuid.newKsuid().toString();
    }
}
