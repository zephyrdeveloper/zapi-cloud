package com.thed.zephyr;

import com.thed.zephyr.cloud.rest.client.JwtGenerator;
import com.thed.zephyr.util.AbstractTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by aliakseimatsarski on 3/15/16.
 */
public class GenerateJWTTest extends AbstractTest {

    private static JwtGenerator jwtGenerator;

    @BeforeClass
    public static void setUp() throws Exception{
        jwtGenerator = client.getJwtGenerator();
    }

    //@Test
    public void testGenerateJWT() throws URISyntaxException {
        String urlStr = "https://ea7decb8.ngrok.io/public/rest/api/1.0/executions?projectId=10200&issueId=11105&offset=0&size=10";
        URI url = new URI(urlStr);
        String jwt = jwtGenerator.generateJWT("GET", url, 3600);
        System.out.println("JWT " + jwt);
    }


}
