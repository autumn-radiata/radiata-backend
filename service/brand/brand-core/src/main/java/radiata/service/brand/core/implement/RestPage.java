package radiata.service.brand.core.implement;

import java.util.List;

public record RestPage<T>(
    List<T> content,
    int size,
    long total
) {

}
