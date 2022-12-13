package com.lv.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditService {

    private final AuditReader auditReader;

    /**
     * Description: Check whether an entity class is audited or not
     *
     * @param clazz<T>: Class Type of entity tobe check
     * @return true if this Class is audited, else false
     */
    public <T extends Object> boolean isAudited(Class<T> clazz) {
        try {
            return auditReader.isEntityClassAudited(clazz);
        } catch (Exception ex) {
            log.error("Checking audit failed, {}", ex.getMessage());
        }
        return false;
    }

    /**
     * Description: Get current (latest) audit version of an entity class
     *
     * @param clazz<T>: Class Type of entity tobe get current version
     * @param <T>
     * @return
     */
    public <T extends Object> Integer getCurrentVersion(Class<T> clazz) {
        try {
            Number currentVersion = (Number) auditReader.createQuery()
                    .forRevisionsOfEntity(clazz, false, true)
                    .addProjection(AuditEntity.revisionNumber().max())
                    .getSingleResult();
            if (currentVersion != null) {
                return currentVersion.intValue();
            }
        } catch (Exception ex) {
            log.error("Get current version for failed, {}", ex.getMessage());
        }
        return 0;
    }

    /**
     * Description: Get current (latest) audit version of a specific entity
     *
     * @param clazz: Class Type of entity tobe get current version
     * @param entityId: Id of entity tobe get current version
     * @param <T>
     * @return
     */
    public <T extends Object> Integer getCurrentVersion(Class<T> clazz, Object entityId) {
        try {
            Number currentVersion = (Number) auditReader.createQuery()
                    .forRevisionsOfEntity(clazz, false, true)
                    .addProjection(AuditEntity.revisionNumber().max())
                    .add(AuditEntity.id().eq(entityId))
                    .getSingleResult();
            if (currentVersion != null) {
                return currentVersion.intValue();
            }
        } catch (Exception ex) {
            log.error("Get current version for object {} failed, {}", entityId, ex.getMessage());
        }
        return 0;
    }

    /**
     * Description: Get previous audit version of a specific version of an entity
     *
     * @param clazz: Class Type of entity tobe get current version
     * @param entityId: Id of entity tobe get previous version
     * @param currentRevision: Audit version
     * @return: previous version Entity Object of @currentVersion
     * @param <T>
     */
    public <T extends Object> T getPreviousVersion(Class<T> clazz, Object entityId, int currentRevision) {
        try {
            Number prevRevision = (Number) auditReader.createQuery()
                    .forRevisionsOfEntity(clazz, false, true)
                    .addProjection(AuditEntity.revisionNumber().max())
                    .add(AuditEntity.id().eq(entityId))
                    .add(AuditEntity.revisionNumber().lt(currentRevision))
                    .getSingleResult();
            if (prevRevision != null) {
                return (T) auditReader.find(clazz, entityId, prevRevision);
            }
        } catch (Exception ex) {
            log.error("Get previous version for object {} failed, {}", entityId, ex.getMessage());
        }
        return null;
    }

    /**
     * Description: Get list previous audit version of a specific version of an entity
     *
     * @param clazz: Class Type of entity tobe get current version
     * @param entityId: Id of entity tobe get previous version
     * @return: previous version Entity Object of @currentVersion
     * @param <T>
     */
    public <T extends Object> List<T> getPreviousVersion(Class<T> clazz, Object entityId) {
        try {
            AuditQuery query = auditReader.createQuery()
                    .forRevisionsOfEntity(clazz, true, true)
                    .add(AuditEntity.id().eq(entityId))
                    .addOrder(AuditEntity.revisionNumber().asc());
            return query.getResultList();
        } catch (Exception ex) {
            log.error("Get previous version for object {} failed, {}", entityId, ex.getMessage());
        }
        return null;
    }
}
