package com.thed.zephyr.cloud.rest;

import com.thed.zephyr.cloud.rest.client.*;
import com.thed.zephyr.cloud.rest.client.impl.*;
import com.thed.zephyr.cloud.rest.model.ZConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by aliakseimatsarski on 3/25/16.
 */
public class ZFJCloudRestClient {

    private JwtGenerator jwtGenerator;

    private Logger log = LoggerFactory.getLogger(ZFJCloudRestClient.class);

    private ZFJCloudRestClient() {
    }

    public static Builder  restBuilder(String zephyrBaseUrl, String accessKey, String secretKey, String userName){
        return new ZFJCloudRestClient().new Builder(zephyrBaseUrl, accessKey, secretKey, userName);
    }

    public JwtGenerator getJwtGenerator(){
        return jwtGenerator;
    }

    public class Builder {

        private String accessKey;
        private String secretKey;
        private String userName;
        private String zephyrBaseUrl;

        private Builder(String zephyrBaseUrl, String accessKey, String secretKey, String userName) {
            this.zephyrBaseUrl = zephyrBaseUrl;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.userName = userName;
        }

        public ZFJCloudRestClient build() {
            ZConfig zConfig = new ZConfig(accessKey, secretKey, userName, zephyrBaseUrl);
            jwtGenerator = new JwtGeneratorImpl(zConfig);

            return ZFJCloudRestClient.this;
        }
    }
}
