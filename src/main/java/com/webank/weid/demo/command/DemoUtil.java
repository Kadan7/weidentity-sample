/*
 *       Copyright© (2019) WeBank Co., Ltd.
 *
 *       This file is part of weidentity-sample.
 *
 *       weidentity-sample is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weidentity-sample is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weidentity-sample.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.webank.weid.demo.command;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.demo.common.util.FileUtil;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.protocol.base.Credential;

/**
 * the DemoUtil for command.
 *
 */
public class DemoUtil {

    private static final Logger logger = LoggerFactory.getLogger(DemoUtil.class);

    /**
     * temporary file directory.
     */
    private static final String TEMP_DIR = "./tmp/";

    /**
     * credential data file.
     */
    private static final String CRED_FILE = TEMP_DIR + "credential.json";

    /**
     * temporary data file.
     */
    private static final String TEMP_FILE = TEMP_DIR + "temp.data";

    /**
     *  SDK private key.
     */
    public static final String SDK_PRIVATE_KEY;

    /**
     * Hexadecimal.
     */
    private static final int HEXADECIMAL = 16;

    static {

        // check the temporary file directory, create it when it does not exists.
        FileUtil.checkDir(TEMP_DIR);

        // getting SDK private key information from a file.
        String sdkPrivateKey = FileUtil.getDataByPath(PrivateKeyUtil.SDK_PRIVKEY_PATH);

        // converting a hexadecimal private key to a digital private key.
        SDK_PRIVATE_KEY = new BigInteger(sdkPrivateKey, HEXADECIMAL).toString();
    }

    /**
     * read credential.
     * @return object of Credential
     */
    public static Credential getCredentialFromJson() {

        Credential credential = null;

        // get credential string from the file.
        String jsonStr = FileUtil.getDataByPath(CRED_FILE);
        
        // converting credential string to Credential object.
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            credential = objectMapper.readValue(jsonStr, Credential.class);
        } catch (IOException e) {
            logger.error("getCredentialFromJson error", e);
        }
        return credential;
    }

    /**
     * get temporary data from file.
     * 
     * @return return map format data
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getTempDataFromFile() {

        // getting authority data from temporary file.
        String json = FileUtil.getDataByPath(DemoUtil.TEMP_FILE);
        ObjectMapper om = new ObjectMapper();
        Map<String, String> paramMap = null;
        try {
            paramMap = om.readValue(json, Map.class);
        } catch (IOException e) {
            logger.error("read temp.data error", e);
        }
        return paramMap;
    }

    /**
     * save credential in a specified file.
     * 
     * @param credential require
     * @return return to save path
     */
    public static String saveCredential(Credential credential) {

        // converting Credential object to JSON string.
        String dataStr = DemoUtil.formatObjectToString(credential);

        // save the JSON string in the file.
        return FileUtil.saveFile(CRED_FILE, dataStr);
        
    }

    /**
     * save temporary data.
     * 
     * @param map require
     */
    public static void saveTemData(Map<String, String> map) {

        // converting Map to JSON string.
        String dataStr = DemoUtil.formatObjectToString(map);

        // save the JSON string in the file.
        FileUtil.saveFile(TEMP_FILE, dataStr);
    }

    /**
     * format Object to String.
     * 
     * @return return JSON string
     */
    public static String formatObjectToString(Object obj) {

        ObjectMapper mapper = new ObjectMapper();
        String dataStr = "";
        try {
            dataStr = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("writeValueAsString error:", e);
        }
        return dataStr;
    }
}
