package stacs.eden.datastore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class DataFeedStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dataFeedStoreId;
    private String signPayload;
    private String txJson;
    private String submitterAddress;

    public DataFeedStore() {

    }

    public DataFeedStore(String signPayload,
                         String txJson,
                         String submitterAddress) {
        this.signPayload=signPayload;
        this.txJson=txJson;
        this.submitterAddress=submitterAddress;
    }
}
