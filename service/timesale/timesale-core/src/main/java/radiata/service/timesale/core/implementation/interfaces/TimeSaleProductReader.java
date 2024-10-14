package radiata.service.timesale.core.implementation.interfaces;

import radiata.service.timesale.core.domain.TimeSaleProduct;

public interface TimeSaleProductReader {

    TimeSaleProduct read(String timeSaleProductId);
}
