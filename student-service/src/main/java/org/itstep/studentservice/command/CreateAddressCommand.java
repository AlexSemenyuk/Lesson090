package org.itstep.studentservice.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record CreateAddressCommand(
        @NotBlank
        @Size(min = 1, max = 50)
        String country,

        @NotBlank
        @Size(min = 1, max = 50)
        String city,

        @NotBlank
        @Size(min = 1, max = 100)
        String addressLine1,

        String addressLine2
) {
}
