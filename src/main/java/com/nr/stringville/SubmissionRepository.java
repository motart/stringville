package com.nr.stringville;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findById(Long id);
}
