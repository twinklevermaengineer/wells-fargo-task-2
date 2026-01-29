package com.wellsfargo.counselor.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "advisor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advisor {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "advisor_id")
    private Long advisorId;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank
    @Column(name = "phone", nullable = false)
    private String phone;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;
  
    @OneToMany(mappedBy = "advisor")
    private List<Client> clients = new ArrayList<>();

}