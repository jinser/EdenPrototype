package stacs.eden.api.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TxPrepareObj {
    private String signPayload;
    private Integer docId;

    public TxPrepareObj() {

    }

    public TxPrepareObj(String signPayload,
                        Integer docId) {
        this.signPayload=signPayload;
        this.docId = docId;
    }
}
