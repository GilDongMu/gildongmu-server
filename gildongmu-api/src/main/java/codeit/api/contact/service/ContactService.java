package codeit.api.contact.service;

import codeit.api.contact.dto.request.ContactRequest;
import codeit.api.contact.dto.response.ContactResponse;
import codeit.domain.contact.entity.Contact;
import codeit.domain.contact.repository.ContactRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public void create(ContactRequest request, User user) {
        contactRepository.save(Contact.builder()
                .user(user)
                .content(request.getContent())
                .build());
    }

    public Slice<ContactResponse> retrieve(Pageable pageable){
        return contactRepository.findByOrderByIdDesc(pageable)
                .map(ContactResponse::from);
    }
}
