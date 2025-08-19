package com.example.dms.configuration;

import com.example.dms.model.Document;
import jakarta.persistence.EntityManager;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class IndexInitializer implements CommandLineRunner {

    private final EntityManager entityManager;

    public IndexInitializer(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run(String... args) throws Exception {
        Search.mapping(entityManager.getEntityManagerFactory())
                .scope(Document.class)
                .massIndexer()
                .startAndWait();
    }
}

