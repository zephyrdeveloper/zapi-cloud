package com.thed.zephyr.util;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aliakseimatsarski on 3/17/16.
 */
public class AbstractTest {

    final static String accessKey = "amlyYTo5OTY4ZGJiMy0yYzY3LTQyNzQtOGEyZC0wYjQwMGViOGQ0YjYgYWRtaW4gYWxleC1rZXk";/*replace with you credentials */
  //  final static String accessKey = "amlyYTo5OTY4ZGJiMy0yYzY3LTQyNzQtOGEyZC0wYjQwMGViOGQ0YjYgYWRtaW4";

    final static String secretKey = "PCaxEBZKIkwRdHWocjuIIdbH2hRR7TvwL2RO5T7wjdY";/*replace with you credentials */
 //   final static String secretKey = "ezBkGY4V0fnNyE3mAMNl813rhxqM5c79fijbdlf3eZQ";

    final static String userName = "admin";
    final static String zephyrBaseUrl = "https://ea7decb8.ngrok.io";
    public static ZFJCloudRestClient client;

    public Logger log = LoggerFactory.getLogger(AbstractTest.class);

    static {
        client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName).build();
    }
}
