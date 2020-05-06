package stacs.eden.chain;

import com.alibaba.fastjson.JSONObject;
import hashstacs.sdk.dto.DataFeed;
import hashstacs.sdk.request.blockchain.DataFeedReqBO;
import hashstacs.sdk.response.base.JsonRespBO;
import hashstacs.sdk.util.ChainConnector;
import stacs.eden.util.PropertiesUtil;

public class SendDataFeed {
    private static String CONFIG_PROPERTIES = "application-dev.properties";

    private static ChainConnector _chainConn;
    private DataFeed dataFeedTxReq;
    private String merchantAesKey;
    private String merchantId;
    private String stacsChainUrl;
    private static String bdCode;

    public SendDataFeed() {
        merchantAesKey = PropertiesUtil.getConfigProperty(CONFIG_PROPERTIES,PropertiesUtil.ConfigEnums.MERCHANT_AESKEY);
        merchantId=PropertiesUtil.getConfigProperty(CONFIG_PROPERTIES,PropertiesUtil.ConfigEnums.MERCHANT_ID);
        stacsChainUrl=PropertiesUtil.getConfigProperty(CONFIG_PROPERTIES,PropertiesUtil.ConfigEnums.MERCHANT_URL);
        bdCode=PropertiesUtil.getConfigProperty(CONFIG_PROPERTIES,PropertiesUtil.ConfigEnums.BD_CODE);
        _chainConn = ChainConnector.initConn(merchantAesKey,merchantId,stacsChainUrl);
    }

    public String createUnsignedTx(String address, String payload) {
        dataFeedTxReq = new DataFeed(bdCode);

        dataFeedTxReq.setSubmitterAddress(address);
        dataFeedTxReq.setVersion("1");
        dataFeedTxReq.setDataPayload(payload);

        DataFeedReqBO unsignedReq = new DataFeedReqBO(dataFeedTxReq);
        unsignedReq.generateTxId();

        return unsignedReq.generateOfflinePayloadForSigning();
    }

    public JSONObject getUnsignedReq() {

        return dataFeedTxReq.getReqObj();
    }

    public String signTxAndSend(JSONObject unsignedPayload, String signature) {

        DataFeed signedFeed = new DataFeed(unsignedPayload);
        signedFeed.setSubmitterSignature(signature);
        JsonRespBO response = _chainConn.sendDataFeed(signedFeed);

        if(response.getIsSuccessful()) {
            return response.getTxId();
        }

        return null;
    }

}
