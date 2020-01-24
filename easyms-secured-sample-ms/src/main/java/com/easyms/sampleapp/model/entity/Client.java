package com.easyms.sampleapp.model.entity;

import com.easyms.sampleapp.utils.SampleAppMessages;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString()
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @NotNull
    @Getter @Setter
    private String firstname;
    @NotNull
    @Getter @Setter
    private String lastname;
    @NotNull
    @Email(
            message = SampleAppMessages.INVALID_EMAIL
    )
    @Getter @Setter
    @Column(unique = true)
    private String email;
}
