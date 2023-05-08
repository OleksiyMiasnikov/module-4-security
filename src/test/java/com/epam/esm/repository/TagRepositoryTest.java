package com.epam.esm.repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TagRepositoryTest {
//    private final JdbcTemplate jdbcTemplate  = new JdbcTemplate(dataSource());
//    private final TagRepository repo = new TagRepository(jdbcTemplate);
//
//    private DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
//        return dataSource;
//    }
//
//    @BeforeEach
//    void init() {
//        String CREATE_TABLE = "CREATE TABLE tag (" +
//                "  id INT AUTO_INCREMENT," +
//                "  name VARCHAR(45)," +
//                "  PRIMARY KEY (id))";
//        jdbcTemplate.update(CREATE_TABLE);
//        repo.create(Tag.builder().name("tag_1").build());
//        repo.create(Tag.builder().name("tag_2").build());
//        repo.create(Tag.builder().name("tag_3").build());
//    }
//    @AfterEach
//    void tearEach(){
//        String DROP_TABLE = "DROP TABLE tag";
//        jdbcTemplate.update(DROP_TABLE);
//    }
//
//    @Test
//    void create() {
//        String name = "test-tag";
//        int createdId = repo.create(Tag.builder().name(name).build());
//        assertThat(createdId).isNotNegative();
//    }
//
//    @Test
//    void findAll() {
//        List<Tag> expectedList = new LinkedList<>();
//        expectedList.add(Tag.builder()
//                .id(1)
//                .name("tag_1")
//                .build());
//        expectedList.add(Tag.builder()
//                .id(2)
//                .name("tag_2")
//                .build());
//        expectedList.add(Tag.builder()
//                .id(3)
//                .name("tag_3")
//                .build());
//        assertThat(repo.findAll()).isEqualTo(expectedList);
//    }
//
//    @Test
//    void findById() {
//        Tag expectedTag = Tag.builder()
//                .id(2)
//                .name("tag_2")
//                .build();
//        Optional<Tag> actual = repo.findById(2);
//        assertThat(actual).isPresent();
//        assertThat(actual.get()).isEqualTo(expectedTag);
//    }
//
//    @Test
//    void findByName() {
//        List <Tag> expected = new LinkedList<>(List.of(Tag.builder()
//                                                        .id(2)
//                                                        .name("tag_2")
//                                                        .build()));
//        assertThat(repo.findByName("tag_2")).isEqualTo(expected);
//    }
//
//    @Test
//    void delete() {
//        assertThat(repo.delete(1)).isTrue();
//        assertThat(repo.delete(1)).isFalse();
//    }
}