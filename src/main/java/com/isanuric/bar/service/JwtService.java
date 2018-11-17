package com.isanuric.bar.service;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */

import com.isanuric.bar.utils.Utils;
import org.jose4j.base64url.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtService {

    private final static Logger logger = LoggerFactory.getLogger(JwtService.class);

    @SuppressWarnings("unchecked")
    public String buildRequestToken(String user) {

        JsonWebSignature jws = buildRequestHeader();
        JSONObject payload = buildRequestBody(user);
        jws.setPayload(payload.toJSONString());

        // Set the verification key
        HmacKey key = new HmacKey(Base64.decode("EWGTENJ7FGBVFDWE45T6KM6VF196H6ZAJ7FGBVFDWE45T6KM6VF196H6ZA"));
        logger.debug("key: {}", key);
        jws.setKey(key);

        String jwsCompactSerialization = null;
        try {
            jwsCompactSerialization = jws.getCompactSerialization();
        } catch (JoseException e) {
            e.printStackTrace();
        }

        logger.debug("token: {}", jwsCompactSerialization);
        return jwsCompactSerialization;
    }

    @SuppressWarnings("unchecked")
    public JsonWebSignature buildRequestHeader() {

        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setHeader("typ", "JWT");
        return jws;
    }

    @SuppressWarnings("unchecked")
    public JSONObject buildRequestBody(String user) {

        JSONObject payload = new JSONObject();
        payload.put("user", user);
        payload.put("iss", "test");
        payload.put("exp", Utils.getCurrentTimeStamp() + 1_000_000L);
        return payload;
    }


}
