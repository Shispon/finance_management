
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer>{

    private static final String IMAGE_VERSION = "postgres:15-alpine";
    private static PostgresTestContainer container;

    private PostgresTestContainer() {
        super(DockerImageName.parse(IMAGE_VERSION));
    }

    public static synchronized PostgresTestContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {

    }
}
