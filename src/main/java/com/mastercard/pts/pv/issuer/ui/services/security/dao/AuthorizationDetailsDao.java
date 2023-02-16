package com.mastercard.pts.pv.issuer.ui.services.security.dao;

import com.mastercard.pts.pv.issuer.ui.services.security.model.Entitlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuthorizationDetailsDao extends JpaRepository<EntitlementEntity,Integer> {

    @Query(value ="select distinct ent.PLCY_RSRC_NAM AS entitlement" +
            " from ENT ent" +
            " INNER JOIN ROLE_ENT roleEnt        ON ent.ENT_ID=roleEnt.ENT_ID" +
            " INNER JOIN IF_ROLE role            ON role.IF_ROLE_ID=roleEnt.IF_ROLE_ID" +
            " INNER JOIN USER_ROLE userRole      ON userRole.IF_ROLE_ID=role.IF_ROLE_ID" +
            " INNER JOIN USER_REPOSITORY usrRepo ON userRole.USER_ID=userRole.USER_ID" +
            " where role.PLCY_RSRC_NAM in(:roleNames)" +
            " AND usrRepo.REP_USER_ID=:userLdapId",nativeQuery = true)
    public List<Entitlement> findEntitlements(@Param("roleNames") List<String> roleNames, @Param("userLdapId") String userLdapId);
}
