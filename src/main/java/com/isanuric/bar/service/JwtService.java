package com.isanuric.bar.service;

/*
 * Project: bar
 * @author ehsan.salmani
 */

import com.isanuric.bar.utils.Const;
import com.isanuric.bar.utils.Utils;
import org.jose4j.base64url.Base64;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class JwtService {

    private final static Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.key}")
    private String jwtKey;

    @SuppressWarnings("unchecked")
    public String buildJwsToken(String user) {

        JsonWebSignature jws = buildRequestHeader();
        JSONObject payload = buildRequestBody(user);
        jws.setPayload(payload.toJSONString());

        // Set the verification key
        HmacKey key = new HmacKey(Base64.decode(jwtKey));
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

    public JSONObject verifyToken(String jws) {

        JSONParser parser = new JSONParser();
        JSONObject responsePayloadJSON = null;

        try {

            JsonWebSignature responseJWS = new JsonWebSignature();
            responseJWS.setCompactSerialization(jws.replace(Const.TOKEN_PREFIX, ""));
            HmacKey key = new HmacKey(Base64.decode(jwtKey));
            responseJWS.setKey(key);
            if (!responseJWS.verifySignature()) {
                // TODO: 18/11/2018
//                throw new InvalidJwtSignatureException();
                return null;
            }
            responsePayloadJSON = (JSONObject) parser.parse(responseJWS.getPayload());

            if (responsePayloadJSON.containsKey("responseBody")) {
                responsePayloadJSON = (JSONObject) responsePayloadJSON.get("responseBody");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responsePayloadJSON;
    }


}
