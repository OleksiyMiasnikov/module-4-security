package com.epam.esm.util;

import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.DTO.tag.CreateTagRequest;
import com.epam.esm.model.DTO.user.CreateUserRequest;
import com.epam.esm.model.DTO.user_order.CreateUserOrderRequest;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.*;
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

    public void parseFileWithTags() throws FileNotFoundException {
        File file = new File("db//spring-core.txt");
        Scanner input = new Scanner(file);
        int count = 0;
        Set<String> wordsSet = new TreeSet<>();
        while (input.hasNext() && count < 1000) {
            StringBuilder word  = new StringBuilder(input.next().replaceAll("[^A-Za-z]", ""));
            if (word.length() < 4 || word.length() > 10) continue;
            if (! wordsSet.add(word.toString().toUpperCase())) continue;
            count++;
        }
        wordsSet.forEach(w -> tagService.create(CreateTagRequest.builder().name(w).build()));
        System.out.println("Tags " + count);
    }

    public void parseFileWithNames() throws FileNotFoundException {
        File file = new File("db//persons-names.txt");
        Scanner input = new Scanner(file);
        int count = 0;
        Set<String> wordsSet = new TreeSet<>();
        while (input.hasNext() && count < 1000) {
            StringBuilder word  = new StringBuilder(input.next().replaceAll("[^A-Za-z]", ""));
            if (word.length() < 4 || word.length() > 10) continue;
            if (! wordsSet.add(word.toString().toUpperCase())) continue;
            count++;
        }
        wordsSet.forEach(w -> userService.create(CreateUserRequest.builder()
                        .password("111")
                        .username(w)
                        .build()));
        System.out.println("Users " + count);
    }

    public void createCertificatesWithTags() {
        List<Tag> tags = tagService.findByName("");
        List<User> users = userService.findAll(Pageable.ofSize(10000).withPage(0)).toList();
        System.out.println("Tags - " + tags.size());
        System.out.println("Users - " + users.size());
        Random random = new Random();
        for (int count = 0; count < 10000; count++) {
            Tag tag = tags.get(random.nextInt(tags.size()));
            String certificateName = "Certificate #" + count;
            CertificateWithTagRequest request = CertificateWithTagRequest.builder()
                    .tag(tag.getName())
                    .name(certificateName)
                    .description(certificateName + " with tag:" + tag.getName() + " description")
                    .price(random.nextInt(100000)/100.0)
                    .duration(random.nextInt(180))
                    .build();
            CertificateWithTag certificateWithTag = certificateWithTagService.create(request);
            userOrderService.create(CreateUserOrderRequest.builder()
                            .certificateWithTagId(certificateWithTag.getId())
                            .userId(users.get(random.nextInt(users.size())).getId())
                            .build());
        }
        System.out.println("Orders done!");
    }
}
