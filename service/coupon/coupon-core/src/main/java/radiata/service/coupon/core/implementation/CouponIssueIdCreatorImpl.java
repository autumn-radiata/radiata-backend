package radiata.service.coupon.core.implementation;

import com.github.ksuid.Ksuid;
import radiata.common.annotation.Implementation;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueIdCreator;

@Implementation
public class CouponIssueIdCreatorImpl implements CouponIssueIdCreator {

    @Override
    public String create() {

        return Ksuid.newKsuid().toString();
    }
}
