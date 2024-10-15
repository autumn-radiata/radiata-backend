package radiata.service.brand.core.model.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.service.brand.core.model.constant.ColorType;
import radiata.service.brand.core.model.constant.GenderType;
import radiata.service.brand.core.model.constant.SizeType;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
@Table(name = "r_product")
@SQLRestriction("deleted_at IS NULL")
public class Product extends BaseEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    private String name;

    private Integer price;

    private Integer discountAmount;

    @Embedded
    private Stock stock;

    private GenderType gender;

    private ColorType color;

    private SizeType size;


    public static Product of(String id, Brand brand, Category category, String name, Integer price,
        Integer discountAmount, Integer stock, GenderType gender, ColorType color, SizeType size) {
        return Product.builder()
            .id(id)
            .brand(brand)
            .category(category)
            .name(name)
            .price(price)
            .discountAmount(discountAmount)
            .stock(Stock.from(stock))
            .gender(gender)
            .color(color)
            .size(size)
            .build();
    }

    public void updateInfo(Category category, String name, Integer price, Integer discountAmount, Integer stock,
        GenderType gender,
        ColorType color, SizeType size) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.discountAmount = discountAmount;
        this.stock = Stock.from(stock);
        this.gender = gender;
        this.color = color;
        this.size = size;
    }

    public void delete() {
        deleteEntity();
    }

    public void setMaxDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void subStock(int quantity) {
        this.stock = stock.subStock(quantity); // 반환된 새로운 객체를 저장
    }

    public void addStock(int quantity) {
        this.stock = stock.addStock(quantity);
    }
}
