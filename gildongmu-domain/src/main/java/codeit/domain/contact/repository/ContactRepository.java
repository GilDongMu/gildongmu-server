package codeit.domain.contact.repository;

import codeit.domain.contact.entity.Contact;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Slice<Contact> findByOrderByIdDesc(Pageable pageable);
}
