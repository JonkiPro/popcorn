package com.jonki.popcorn.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.annotation.Nullable;

/**
 * Base fields for multiple DTOs.
 */
@Getter
@EqualsAndHashCode(of = "id", callSuper = false, doNotUseGetters = true)
@ApiModel(description = "The base object")
public abstract class BaseDTO extends CommonResource{

    private static final long serialVersionUID = 5144674981062625160L;

    @ApiModelProperty(notes = "The ID", required = true)
    private final String id;

    /**
     * Constructor.
     *
     * @param builder The builder to use
     */
    BaseDTO(final Builder builder) {
        this.id = builder.bId;
    }

    /**
     * Builder pattern to save constructor arguments.
     */
    @SuppressWarnings("unchecked")
    public abstract static class Builder<T extends Builder> {

        private String bId;

        protected Builder() {
        }

        /**
         * Set the id for the resource.
         *
         * @param id The id
         * @return The builder
         */
        public T withId(@Nullable final String id) {
            this.bId = id;
            return (T) this;
        }
    }
}
