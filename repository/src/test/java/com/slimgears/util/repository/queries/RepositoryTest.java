package com.slimgears.util.repository.queries;

import com.slimgears.util.repository.query.DefaultRepository;
import com.slimgears.util.repository.query.QueryProvider;
import com.slimgears.util.repository.query.Repository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RepositoryTest {
    @Mock private QueryProvider mockQueryProvider;
    private Repository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repository = new DefaultRepository(mockQueryProvider);
    }

    @Test
    public void testRepositoryQuery() {
        repository.collection(TestEntity.metaClass)
                .filter(TestEntity.$.key.eq(TestKey.create("aaa"))
                        .and(TestEntity.$.number.greaterThan(5))
                        .and(TestEntity.$.refEntity.text.contains("bbb")))
                .flatMap(TestEntity.$.refEntities)
                .filter(TestRefEntity.$.text.endsWith("ccc"))
                .map(TestRefEntity.$.text)
                .retrieve()
                .execute();
    }
}