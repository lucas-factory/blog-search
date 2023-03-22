package com.lucas.platform.searchcore.common.request;

import com.lucas.platform.searchcore.annotation.SortType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchWebReq {

    @NotEmpty
    @Length(min = 1, max = 128)
    private String query;

    @SortType
    private String sort = "accuracy";

    @Range(min = 1, max = 50)
    private int page = 1;

    @Range(min = 1, max = 50)
    private int size = 10;

}
