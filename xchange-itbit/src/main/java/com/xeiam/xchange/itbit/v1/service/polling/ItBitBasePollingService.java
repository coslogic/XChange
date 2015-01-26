package com.xeiam.xchange.itbit.v1.service.polling;

import java.io.IOException;
import java.util.List;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;
import si.mazi.rescu.SynchronizedValueFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.itbit.v1.ItBitAuthenticated;
import com.xeiam.xchange.itbit.v1.service.ItBitHmacPostBodyDigest;
import com.xeiam.xchange.service.BaseExchangeService;
import com.xeiam.xchange.service.polling.BasePollingService;

public class ItBitBasePollingService extends BaseExchangeService implements BasePollingService {

  protected final SynchronizedValueFactory<Long> nonceFactory;

  protected final String apiKey;
  protected final ItBitAuthenticated itBit;
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   * @param nonceFactory
   */
  public ItBitBasePollingService(Exchange exchange, SynchronizedValueFactory<Long> nonceFactory) {

    super(exchange);

    this.nonceFactory = nonceFactory;
    this.itBit = RestProxyFactory.createProxy(ItBitAuthenticated.class, (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("authHost"));
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = ItBitHmacPostBodyDigest.createInstance(apiKey, exchange.getExchangeSpecification().getSecretKey());
  }

  @Override
  public List<CurrencyPair> getExchangeSymbols() throws IOException {

    return exchange.getMetaData().getCurrencyPairs();
  }

}
