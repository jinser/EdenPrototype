package stacs.eden.api;

import com.alibaba.fastjson.JSONObject;
import hashstacs.sdk.response.blockchain.DataFeedQueryRespBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stacs.eden.api.response.TxPrepareObj;
import stacs.eden.chain.QueryTx;
import stacs.eden.chain.SendDataFeed;
import stacs.eden.datastore.entity.DataFeedStore;
import stacs.eden.datastore.service.DataFeedStoreService;
import stacs.eden.util.DocHashUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@RestController
public class InvoiceMgmtAPI {
    private static final Logger _logger = LoggerFactory.getLogger(InvoiceMgmtAPI.class);

    @Autowired
    DataFeedStoreService _dataFeedStore;

    @CrossOrigin
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        _logger.info("health check completed.");
        return  new ResponseEntity<>("health check has been successful.", HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/v1/uploadDocumentHash")
    public ResponseEntity<TxPrepareObj> uploadDocumentHash(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("address") String address) {
        //validate file
        if (file.isEmpty()) {
            return new ResponseEntity("File is empty. ", HttpStatus.BAD_REQUEST);
        } else {

            // parse file
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                //convert file to Base64 bytes
                byte[] encoded = java.util.Base64.getEncoder().encode(file.getBytes());
                //create document hash and an AES Key from the address for encryption
                String docHash = DocHashUtil.createDocumentHash(address,encoded.toString());

                //create the JsonObject to be sent to the chain
                SendDataFeed dataReq = new SendDataFeed();
                String signPayload = dataReq.createUnsignedTx(address,docHash);

                //store the request in the database
                String requestObjAsString = dataReq.getUnsignedReq().toString();
                Integer docId  = _dataFeedStore.addDataFeed(signPayload,requestObjAsString,address);

                //return the signature payload
                TxPrepareObj response = new TxPrepareObj(signPayload,docId);

                return new ResponseEntity(response, HttpStatus.OK);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping("/v1/uploadSignature")
    public ResponseEntity uploadDocumentSignature(@RequestParam("Signature") String signature,
                                                  @RequestParam("DocId") String docId) {
        //get document details by docId from DB
        DataFeedStore doc = _dataFeedStore.getDataFeedById(Integer.parseInt(docId));

        //reconstruct JSON payload of document, add signature and send to chain
        SendDataFeed signedFeed = new SendDataFeed();
        String txid = signedFeed.signTxAndSend((JSONObject) JSONObject.parse(doc.getTxJson()),signature);

        if(txid!=null) {
            if(!txid.isEmpty()) {
                return new ResponseEntity(txid,HttpStatus.OK);
            }
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @PostMapping("/v1/validateDocumentHash")
    public ResponseEntity validateDocumentHash(@RequestParam("file") MultipartFile file,
                                               @RequestParam("txid") String txId) {
        //get document hash from chain with txid
        QueryTx queryChain = new QueryTx();
        DataFeedQueryRespBO resp = queryChain.checkDataFeedFromChain(txId);

        //retrieve the info of the file used to create the hash
        String hash = resp.getDataFeedInfo().getDataPayload();
        byte[] encoded = java.util.Base64.getEncoder().encode(hash.getBytes());

        //recreate the hash
        String uploadedDocHash = DocHashUtil.createDocumentHash(resp.getSubmitterAddress(),encoded.toString());

        //validate the document hash with that of the chain
        if(uploadedDocHash.compareTo(hash)==0) {
            return new ResponseEntity("Document matches hash.",HttpStatus.OK);
        }

        return new ResponseEntity("Document does not match hash",HttpStatus.OK);
    }


}
