package com.hunt.worker-service-root;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@NoRepositoryBean
public abstract class DatabaseTestBuilder<T, T_ID> extends TestBuilder<T> {

    @Autowired
    private CrudRepository<T, T_ID> crudRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T build(TestBuilderConfiguration configuration) {
        T testObject = super.build();

        if (configuration.isClear()) {
            log.info("Clearing table = {}.", testObject.getClass().getSimpleName());
            crudRepository.deleteAll();
        }

        if (configuration.isSaved()) {
            log.info("Saving test object = {}.", testObject);
            return crudRepository.save(testObject);
        }

        return testObject;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<T> buildList(TestBuilderConfiguration configuration) {
        List<T> testObjects = super.buildList();

        if (configuration.isClear()) {
            log.info("Clearing table = {}.", testObjects.get(0).getClass().getSimpleName());
            crudRepository.deleteAll();
        }

        if (configuration.isSaved()) {
            log.info("Saving test objects with size = {}.", testObjects.size());
            Iterable<T> savedTestObjects = crudRepository.saveAll(testObjects);
            return StreamSupport.stream(savedTestObjects.spliterator(), false).collect(Collectors.toList());
        }

        return testObjects;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void clear() {
        log.info("Clearing table...");
        crudRepository.deleteAll();
    }
}
