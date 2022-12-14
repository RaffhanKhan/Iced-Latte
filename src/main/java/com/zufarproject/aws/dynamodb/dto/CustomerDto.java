package com.zufarproject.aws.dynamodb.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @NotBlank(message = "CustomerId is mandatory")
    @Size(max = 55, message = "CustomerId length must be less than 55 characters")
    private String customerId;

    @NotBlank(message = "FirstName is mandatory")
    @Size(max = 55, message = "FirstName length must be less than 55 characters")
    private String firstName;

    @NotBlank(message = "LastName is mandatory")
    @Size(max = 55, message = "LastName length must be less than 55 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Valid
    @NotNull(message = "Address is mandatory")
    private AddressDto address;
}
