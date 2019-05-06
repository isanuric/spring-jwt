package com.isanuric.bar.service;

/*
 * Project: bar
 * @author ehsan.salmani
 */

import static org.jose4j.jwa.AlgorithmConstraints.ConstraintType.WHITELIST;

import com.isanuric.bar.utils.Const;
import com.isanuric.bar.utils.Utils;
import java.security.Key;
import org.jose4j.base64url.Base64;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class JwtService {

    private final static Logger logger = LoggerFactory.getLogger(JwtService.class);

    private long expirationTime;

    @Value("${jwt.key}")
    private String jwtKey;

    /**
     * Creates a JWS token, that can be used as HEADER AUTHORIZATION value.
     */
    public String createJwsToken(String user) {

        JsonWebSignature jws = buildRequestHeader();
        JSONObject payload = buildRequestBody(user);
        jws.setPayload(payload.toJSONString());
        jws.setKey(getHmacKey());

        String sigendValue = null;
        try {
            // Sign and produce the JWS Compact Serialization.
            sigendValue = jws.getCompactSerialization();
        } catch (JoseException e) {
            e.printStackTrace();
        }

        logger.debug("token: {}", sigendValue);
        return sigendValue;
    }

    public JsonWebSignature buildRequestHeader() {

        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setHeader("typ", "JWT");
        return jws;
    }

    public JSONObject buildRequestBody(String user) {

        JSONObject payload = new JSONObject();
        payload.put("user", user);
        payload.put("iss", "test");
        payload.put("exp", Utils.getCurrentTimeStamp() + this.expirationTime);
        return payload;
    }

    /**
     * Verify AUTHORISATION JWS Token.
     */
    public JSONObject verifyToken(String jws) {

        JSONParser parser = new JSONParser();
        JSONObject responsePayloadJSON = null;

        try {

            JsonWebSignature responseJWS = new JsonWebSignature();
            responseJWS.setCompactSerialization(jws.replace(Const.TOKEN_PREFIX, ""));
            HmacKey key = getHmacKey();
            responseJWS.setKey(key);
            if (!responseJWS.verifySignature()) {
//                throw new InvalidJwtSignatureException(jws, );
                throw new Exception();
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

    public String doEncryption() throws JoseException {
        Key key = new AesKey(ByteUtil.randomBytes(16));
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setPayload("testencryption");
        jwe.setKey(key);

        return jwe.getCompactSerialization();
    }

    public String doDecryption(String serializedJwe) throws JoseException {

        Key key = new AesKey(ByteUtil.randomBytes(16));
        JsonWebEncryption jwe = new JsonWebEncryption();

        jwe.setAlgorithmConstraints(
                new AlgorithmConstraints(WHITELIST,
                KeyManagementAlgorithmIdentifiers.A128KW));

        jwe.setContentEncryptionAlgorithmConstraints(
                new AlgorithmConstraints(WHITELIST,
                ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));

        jwe.setKey(key);
        jwe.setCompactSerialization(serializedJwe);

        return jwe.getPayload();
    }

    /**
     * Create verification key.
     */
    private HmacKey getHmacKey() {
        HmacKey key = new HmacKey(Base64.decode(this.jwtKey));
        logger.debug("key: {}", key);
        return key;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

}
