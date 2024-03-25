package codeit.api.post.dto.request;

import codeit.api.post.dto.TripDate;
import codeit.common.validator.EnumValue;
import codeit.domain.post.constant.MemberGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostCreateRequest(
        @NotBlank(message = "Title is required.")
        String title,

        @NotBlank(message = "Destination is required.")
        String destination,

        @NotNull(message = "Trip Date cannot be null")
        TripDate tripDate,

        Short numberOfPeople,

        @NotNull(message = "Member Gender cannot be null")
        @EnumValue(enumClass = MemberGender.class, message = "invalid gender")
        String gender,

        @NotBlank(message = "Content is required.")
        String content,

        List<String> tag,

        List<ImageCreateRequest> images

    ) {
    /*
        public MemberGender getMemberGender() {
                return MemberGender.valueOf(this.gender);
        }

    public Post toEntity(User user){
        return Post.builder()
                .title(title)
                .content(content)
                .destination(destination)
                .startDate(tripDate.get(0))
                .endDate(tripDate.get(tripDate.size() - 1))
                .memberGender(memberGender)
                .participants(numberOfPeople)
                .user(user)
                .build();
    }
    */
}
