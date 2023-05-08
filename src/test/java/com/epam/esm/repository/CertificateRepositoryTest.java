package com.epam.esm.repository;

class CertificateRepositoryTest {
//    private final JdbcTemplate jdbcTemplate  = new JdbcTemplate(dataSource());
//    private final CertificateRepository repo = new CertificateRepository(jdbcTemplate);
//
//    private final Certificate certificate_1 = Certificate.builder()
//                .id(1)
//                .name("certificate_1")
//                .description("description of certificate_1")
//                .price(10.50)
//                .duration(2)
//                .createDate(DateUtil.getDate())
//                .lastUpdateDate(DateUtil.getDate())
//                .build();
//    private final Certificate certificate_2 = Certificate.builder()
//                .id(2)
//                .name("certificate_2")
//                .description("description of certificate_2")
//                .price(120.50)
//                .duration(5)
//                .createDate(DateUtil.getDate())
//                .lastUpdateDate(DateUtil.getDate())
//                .build();
//    private final Certificate certificate_3 = Certificate.builder()
//                .id(3)
//                .name("certificate_3")
//                .description("description of certificate_3")
//                .price(1000.50)
//                .duration(25)
//                .createDate(DateUtil.getDate())
//                .lastUpdateDate(DateUtil.getDate())
//                .build();
//
//    private DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
//        return dataSource;
//    }
//
//    @BeforeEach
//    void init() {
//        String CREATE_TABLE = "CREATE TABLE certificate (" +
//                "  id INT AUTO_INCREMENT," +
//                "  name VARCHAR(45)," +
//                "  description VARCHAR(100)," +
//                "  price DECIMAL(10,2)," +
//                "  duration DECIMAL(10)," +
//                "  create_date VARCHAR(45)," +
//                "  last_update_date VARCHAR(45)," +
//                "  PRIMARY KEY (id))";
//        jdbcTemplate.update(CREATE_TABLE);
//        repo.create(certificate_1);
//        repo.create(certificate_2);
//        repo.create(certificate_3);
//    }
//    @AfterEach
//    void tearEach(){
//        String DROP_TABLE = "DROP TABLE certificate";
//        jdbcTemplate.update(DROP_TABLE);
//    }
//
//    @Test
//    void create() {
//        Certificate certificate = Certificate.builder()
//                .name("certificate_4")
//                .description("description of certificate_4")
//                .price(600.50)
//                .duration(15)
//                .createDate(DateUtil.getDate())
//                .lastUpdateDate(DateUtil.getDate())
//                .build();
//        int createdId = repo.create(certificate);
//        assertThat(createdId).isNotNegative();
//    }
//
//    @Test
//    void findAll() {
//        List<Certificate> expected = new LinkedList<>(List.of(certificate_1, certificate_2, certificate_3));
//        List<Certificate> actual = repo.findAll(1,100);
//        assertThat(actual.size()).isEqualTo(3);
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    @Test
//    void findById() {
//        Optional<Certificate> actual = repo.findById(2);
//        assertThat(actual).isPresent();
//        assertThat(actual.get()).isEqualTo(certificate_2);
//    }
//
//    @Test
//    void update() {
//        repo.update(1, certificate_3);
//        Optional<Certificate> actual = repo.findById(1);
//        assertThat(actual).isPresent();
//        actual.get().setId(3);
//        assertThat(actual.get()).isEqualTo(certificate_3);
//    }
//    @Test
//    void delete() {
//        assertThat(repo.delete(1)).isTrue();
//        assertThat(repo.delete(1)).isFalse();
//    }


}