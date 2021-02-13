package org.knowm.xchange.mxc;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import javax.crypto.Mac;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.RestInvocation;

public class MxcDigest extends BaseParamsDigest {

  private MxcDigest(String sign) throws IllegalArgumentException {
    super(sign, HMAC_SHA_256);
  }

  public static MxcDigest createInstance(String sign) {
    return sign == null ? null : new MxcDigest(sign);
  }

  public String createPostSign(String path, String apiKey, Long reqTime) {
    StringBuilder sb = new StringBuilder(1024);
    sb.append("POST")
        .append('\n')
        .append(path)
        .append('\n')
        .append("api_key=")
        .append(apiKey)
        .append("&req_time=")
        .append(reqTime);

    Mac mac = getMac();
    mac.update(sb.toString().getBytes("UTF-8"));

    return String.format("%0128x", new BigInteger(1, mac.doFinal()));
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    try {
      String path = restInvocation.getInvocationUrl().split(restInvocation.getBaseUrl())[1];
      String query = restInvocation.getQueryString();

      StringBuilder sb = new StringBuilder(1024);
      sb.append(restInvocation.getHttpMethod().toUpperCase())
          .append('\n')
          .append(path)
          .append('\n')
          .append(query);

      Mac mac = getMac();
      mac.update(sb.toString().getBytes("UTF-8"));

      return String.format("%0128x", new BigInteger(1, mac.doFinal()));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Illegal encoding, check the code.", e);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
