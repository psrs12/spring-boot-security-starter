package com.mastercard.pts.pv.issuer.ui.services.security.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntitlementEntity {
    @Id
    String entitlement;
}
