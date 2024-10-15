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
@Table(name = "r_category")
@SQLRestriction("deleted_at IS NULL")
public class Category extends BaseEntity {

    @Id
    private String id;

    private String name;

    private String code;

    private String parentCode;


    public static Category of(String id, String name, String code, String parentCode) {
        return builder()
            .id(id)
            .name(name)
            .code(code)
            .parentCode(parentCode)
            .build();
    }

    public void updateInfo(String name, String code, String parentCode) {
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
    }
}
