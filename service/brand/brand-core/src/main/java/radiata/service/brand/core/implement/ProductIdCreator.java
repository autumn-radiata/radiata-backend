package radiata.service.brand.core.implement;

import com.github.ksuid.Ksuid;
import radiata.common.annotation.Implementation;

@Implementation
public class ProductIdCreator {

    public String create() {
        return Ksuid.newKsuid().toString();
    }

}
