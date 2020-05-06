package stacs.eden.datastore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stacs.eden.datastore.entity.DataFeedStore;

import java.util.List;

@Repository
public interface DataFeedStoreDataAccess extends JpaRepository<DataFeedStore, String> {
    List<DataFeedStore> findBySignPayload(String signPayload);
    DataFeedStore findByDataFeedStoreId(Integer dataFeedStoreId);
}
