package stacs.eden.chain;

import hashstacs.sdk.response.blockchain.DataFeedQueryRespBO;
import hashstacs.sdk.util.ChainConnector;

public class QueryTx {

    private static String DEFAULT_BD_CODE = "SystemBD";
    private static ChainConnector _chainConn;

    public QueryTx() {
        _chainConn = ChainConnector.initConn("stacs-sgc01e7002","STACS-Test","http://4-0-load-balancer-39244487.ap-southeast-1.elb.amazonaws.com:6004/api-dapp/manageAPI");
    }

    public DataFeedQueryRespBO checkDataFeedFromChain(String txId) {
        DataFeedQueryRespBO chainResponse = (DataFeedQueryRespBO) _chainConn.queryDetailsByTxId(txId);
        if(chainResponse==null) {
            return null;
        }
        if(chainResponse.getBlockHeight().isEmpty()) {
            return null;
        }
        return chainResponse;
    }
}
