package radiata.service.brand.core.model.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer discountAmount;

    @Embedded
    @Column(nullable = false)
    private Stock stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ColorType color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    public void updateInfo(Category category, Brand brand, String name, Integer price, Integer discountAmount,
        Integer stock,
        GenderType gender,
        ColorType color, SizeType size) {
        this.category = category;
        this.brand = brand;
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
