package radiata.service.brand.core.model.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
@Table(name = "r_brand")
@SQLRestriction("deleted_at IS NULL")
public class Brand extends BaseEntity {

    @Id
    private String id;

    private String name;

    public static Brand of(String id, String name) {
        return Brand.builder()
            .id(id)
            .name(name)
            .build();
    }

    public void updateInfo(String name) {
        this.name = name;
    }
}
