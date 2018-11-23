package com.isanuric.bar.service;

/*
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
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
    public String buildRequestToken(String user) {

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
            logger.debug("TESTTTTTTT");
            responsePayloadJSON = (JSONObject) parser.parse(responseJWS.getPayload());

            // workaround for PingID API 4.5 beta
            if (responsePayloadJSON.containsKey("responseBody")) {
                responsePayloadJSON = (JSONObject) responsePayloadJSON.get("responseBody");
            }

//            if (responsePayloadJSON.get("exp").) {
//                ;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responsePayloadJSON;

//        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
//                .setRequireExpirationTime() // the JWT must have an expiration time
//                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
//                .setRequireSubject() // the JWT must have a subject claim
//                .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
//                .setExpectedAudience("Audience") // to whom the JWT is intended for
//                .setVerificationKey(jwtKey) // verify the signature with the public key
//                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
//                        new AlgorithmConstraints(ConstraintType.WHITELIST, // which is only RS256 here
//                                AlgorithmIdentifiers.RSA_USING_SHA256))
//                .build(); // create the JwtConsumer instance
//
//        try
//        {
//            //  Validate the JWT and process it to the Claims
//            JwtClaims jwtClaims = jwtConsumer.processToClaims(jws);
//            System.out.println("JWT validation succeeded! " + jwtClaims);
//        }
//        catch (InvalidJwtException e)
//        {
//            // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
//            // Hopefully with meaningful explanations(s) about what went wrong.
//            System.out.println("Invalid JWT! " + e);
//
//            // Programmatic access to (some) specific reasons for JWT invalidity is also possible
//            // should you want different error handling behavior for certain conditions.
//
//            // Whether or not the JWT has expired being one common reason for invalidity
//            if (e.hasExpired())
//            {
//                System.out.println("JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime());
//            }
//
//            // Or maybe the audience was invalid
//            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID))
//            {
//                System.out.println("JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience());
//            }
//        }
    }


}
