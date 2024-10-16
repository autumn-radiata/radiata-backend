package radiata.service.brand.core.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Column
    private Integer stock;

    @Builder
    public Stock(int stock) {
        this.stock = stock;
    }

    public static Stock from(int stock) {
        return Stock.builder().stock(stock).build();
    }

    public Stock addStock(int quantity) {
        return Stock.from(this.stock + quantity);
    }

    public Stock subStock(int quantity) {
        if (!hasAvailableStock(quantity)) {
            throw new BusinessException(ExceptionMessage.PRODUCT_INVENTORY_LACK);
        }
        return Stock.from(this.stock - quantity);
    }

    public boolean hasAvailableStock(int quantity) {
        return this.stock > quantity;
    }

    public Integer getStock() {
        return this.stock;
    }


}
