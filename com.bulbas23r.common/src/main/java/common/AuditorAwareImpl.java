package common;

import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(name = "org.springframework.data.domain.AuditorAware")
@ConditionalOnProperty(prefix = "app.jpa", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditorAwareImpl implements AuditorAware<String> {

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        // TODO SecurityContextHolder 해결하기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
//
//        Object principal = authentication.getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            return Optional.of(((UserDetails) principal).getUsername());
//        }

        return Optional.empty();
    }
}
