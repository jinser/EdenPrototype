package stacs.eden.util;

import hashstacs.sdk.util.StacsAPIUtil;

public class DocHashUtil {

    public static String createDocumentHash(String address, String filename) {
        String createAesKey = createAesKeyFromAddr(address);
        String docHash = StacsAPIUtil.encrypt(filename,createAesKey);
        return docHash;
    }

    public static Boolean validateDocumentFromHash(String hash, String address) {
        String createAesKey = createAesKeyFromAddr(address);
        String docHash = StacsAPIUtil.decrypt(hash,createAesKey);
        if(docHash != null) {
            return true;
        }
        return false;
    }

    private static String createAesKeyFromAddr(String address) {
        return address.substring(0,16);
    }
}
