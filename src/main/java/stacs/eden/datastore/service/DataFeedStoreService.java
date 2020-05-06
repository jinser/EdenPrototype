package stacs.eden.datastore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stacs.eden.datastore.dao.DataFeedStoreDataAccess;
import stacs.eden.datastore.entity.DataFeedStore;

@Component
public class DataFeedStoreService {
    @Autowired
    DataFeedStoreDataAccess _dataFeedStoreFromDB;

    public DataFeedStoreService() {

    }

    public DataFeedStore getDataFeedBySignPayload(String signPayload) {
        return _dataFeedStoreFromDB.findBySignPayload(signPayload).get(0);
    }

    public DataFeedStore getDataFeedById(Integer dataId) {
        return _dataFeedStoreFromDB.findByDataFeedStoreId(dataId);
    }

    public Integer addDataFeed(String signPayload, String txJson, String submitterAddress) {
        DataFeedStore newDataFeed = new DataFeedStore(signPayload,txJson,submitterAddress);
        DataFeedStore finalObj = _dataFeedStoreFromDB.save(newDataFeed);
        return finalObj.getDataFeedStoreId();
    }


}
