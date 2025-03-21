package common.model;

import common.UserContextHolder;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    private String deletedBy;

    //    @PreUpdate
    public void setDeleted() {
        if (!isDeleted) {
            this.deletedAt = LocalDateTime.now();
            this.isDeleted = true;
            // 이미 deletedBy 값이 없다면, 현재 요청의 사용자 이름을 기록합니다.
            if (this.deletedBy == null) {
                this.deletedBy = UserContextHolder.getCurrentUser();
            }
        }
    }
}
