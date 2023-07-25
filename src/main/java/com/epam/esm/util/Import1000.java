package com.epam.esm.util;

import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsDTO;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsRequest;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.DTO.tag.CreateTagRequest;
import com.epam.esm.model.DTO.user.CreateUserRequest;
import com.epam.esm.model.DTO.user_order.CreateUserOrderRequest;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.CertificateWithTagService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@RequiredArgsConstructor
@Component
public class Import1000 {

    private final UserService userService;
    private final TagService tagService;
    private final CertificateWithTagService certificateWithTagService;
    private final UserOrderService userOrderService;
    private final Pageable pageable = Pageable.ofSize(10000).withPage(0);

    public void parseFileWithTags() throws FileNotFoundException {
        File file = new File("db//spring-core.txt");
        Scanner input = new Scanner(file);

        Set<String> tags = tagService
                .findAllWithPageable(pageable)
                .map(Tag::getName)
                .toSet();
        Set<String> wordsSet = new HashSet<>();
        int count = tags.size();

        while (input.hasNext() && count < 1500) {
            StringBuilder word  = new StringBuilder(input.next().replaceAll("[^A-Za-z]", ""));
            if (word.length() < 4 || word.length() > 10) continue;
            if (tags.contains(word.toString().toUpperCase())) continue;
            wordsSet.add(word.toString().toUpperCase());
            count++;
        }
        wordsSet.forEach(w -> tagService.create(CreateTagRequest.builder().name(w).build()));
        System.out.println("Tags " + count);
    }

    public void parseFileWithNames() throws FileNotFoundException {
        File file = new File("db//persons-names.txt");
        Scanner input = new Scanner(file);
        Set<String> users = userService
                .findAll(pageable)
                .map(User::getName)
                .toSet();
        Set<String> wordsSet = new HashSet<>();
        int count = users.size();

        while (input.hasNext() && count < 1500) {
            StringBuilder word  = new StringBuilder(input.next().replaceAll("[^A-Za-z]", ""));
            if (word.length() < 4 || word.length() > 10) continue;
            if (users.contains(word.toString())) continue;
            wordsSet.add(word.toString());
            count++;
        }
        wordsSet.forEach(w -> userService.create(CreateUserRequest.builder()
                        .password("111")
                        .name(w)
                        .build()));
        System.out.println("Users " + count);
    }

    public void createCertificatesWithTags() {
        List<Tag> tags = tagService.findAllWithPageable(pageable).toList();
        List<User> users = userService.findAll(pageable).toList();
        System.out.println("Tags - " + tags.size());
        System.out.println("Users - " + users.size());
        Random random = new Random();

        for (int count = 0; count < 10000; count++) {
            Tag tag = tags.get(random.nextInt(tags.size()));
            String certificateName = "Certificate #" + count;
            CertificateWithListOfTagsRequest request = CertificateWithListOfTagsRequest.builder()
                    .tags(new String[] {tag.getName()})
                    .name(certificateName)
                    .description(certificateName + " with tag:" + tag.getName() + " description")
                    .price(random.nextInt(100000)/100.0)
                    .duration(random.nextInt(180))
                    .build();
            CertificateWithListOfTagsDTO certificateWithListOfTagsDTO =
                    certificateWithTagService.create(request);
            userOrderService.create(CreateUserOrderRequest.builder()
                            .certificateWithTagId(certificateWithListOfTagsDTO.getId())
                            .userId(users.get(random.nextInt(users.size())).getId())
                            .build());
        }
        System.out.println("Orders done!");
    }
}
