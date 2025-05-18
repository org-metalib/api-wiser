package org.metalib.market.data.ibkr.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.metalib.market.data.ibkr.api.IserverApi;
import org.metalib.market.data.ibkr.model.AlertRequest;
import org.metalib.market.data.ibkr.model.AlertResponse;
import org.metalib.market.data.ibkr.model.AuthStatus;
import org.metalib.market.data.ibkr.model.Contract;
import org.metalib.market.data.ibkr.model.HistoryData;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdAlertActivatePost200Response;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdAlertActivatePostRequest;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdAlertPost200Response;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdAlertsGet200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdOrderOrderIdDelete200Response;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdOrderOrderIdPost200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdOrderPost200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdOrderWhatifPost200Response;
import org.metalib.market.data.ibkr.model.IserverAccountAccountIdOrdersPostRequest;
import org.metalib.market.data.ibkr.model.IserverAccountOrdersGet200Response;
import org.metalib.market.data.ibkr.model.IserverAccountPnlPartitionedGet200Response;
import org.metalib.market.data.ibkr.model.IserverAccountPost200Response;
import org.metalib.market.data.ibkr.model.IserverAccountsGet200Response;
import org.metalib.market.data.ibkr.model.IserverContractConidAlgosGet200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverContractConidInfoAndRulesGet200Response;
import org.metalib.market.data.ibkr.model.IserverContractRulesPost200Response;
import org.metalib.market.data.ibkr.model.IserverContractRulesPostRequest;
import org.metalib.market.data.ibkr.model.IserverMarketdataConidUnsubscribeGet200Response;
import org.metalib.market.data.ibkr.model.IserverMarketdataSnapshotGet200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverMarketdataUnsubscribeallGet200Response;
import org.metalib.market.data.ibkr.model.IserverReplyReplyidPost200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverReplyReplyidPostRequest;
import org.metalib.market.data.ibkr.model.IserverScannerParamsGet200Response;
import org.metalib.market.data.ibkr.model.IserverScannerRunPost200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverSecdefSearchPost200ResponseInner;
import org.metalib.market.data.ibkr.model.IserverSecdefSearchPostRequest;
import org.metalib.market.data.ibkr.model.IserverSecdefStrikesGet200Response;
import org.metalib.market.data.ibkr.model.ModifyOrder;
import org.metalib.market.data.ibkr.model.OrderRequest;
import org.metalib.market.data.ibkr.model.OrderStatus;
import org.metalib.market.data.ibkr.model.ScannerParams;
import org.metalib.market.data.ibkr.model.SecdefInfo;
import org.metalib.market.data.ibkr.model.SetAccount;
import org.metalib.market.data.ibkr.model.Trade;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class IserverHttpClient implements IserverApi {
  final String baseUrl;

  final HttpClient httpClient;

  final ObjectMapper jackson;

  @Override
  public IserverContractRulesPost200Response iserverContractRulesPost(
      IserverContractRulesPostRequest conid) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/contract/rules");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(conid)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverContractRulesPost200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Trade> iserverAccountTradesGet() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/trades");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), info -> {
        if (200 == info.statusCode()) {
          return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofInputStream(), inputStream ->
            (Supplier<ResponseWrapper<List<Trade>>>) () -> {
              try(final var stream = inputStream) {
                return new ResponseWrapper<>(jackson.readValue(stream, new TypeReference<List<Trade>>() {}));
              } catch(IOException e) {
                throw new UncheckedIOException(e);
              }
          });
        } else {
          return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofByteArray(), content ->
                  () -> new ResponseWrapper<>(content) );
        }
      });
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public AlertResponse iserverAccountAlertIdGet(String id) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/alert/:id");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<AlertResponse>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountsGet200Response iserverAccountsGet() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/accounts");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountsGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountAccountIdOrderWhatifPost200Response iserverAccountAccountIdOrdersWhatifPost(
      String accountId, IserverAccountAccountIdOrdersPostRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/{accountId}/orders/whatif");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountAccountIdOrderWhatifPost200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountAccountIdOrderOrderIdDelete200Response iserverAccountAccountIdOrderOrderIdDelete(
      String accountId, String orderId) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/{accountId}/order/{orderId}");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .DELETE();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountAccountIdOrderOrderIdDelete200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountPnlPartitionedGet200Response iserverAccountPnlPartitionedGet() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/pnl/partitioned");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountPnlPartitionedGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountAccountIdAlertActivatePost200Response iserverAccountAccountIdAlertActivatePost(
      String accountId, IserverAccountAccountIdAlertActivatePostRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/:accountId/alert/activate");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountAccountIdAlertActivatePost200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverAccountAccountIdOrderPost200ResponseInner> iserverAccountAccountIdOrderPost(
      String accountId, OrderRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/{accountId}/order");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverAccountAccountIdOrderPost200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SecdefInfo> iserverSecdefInfoGet(String conid, String sectype, String month,
      String exchange, BigDecimal strike, String right) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/secdef/info");
      Optional.ofNullable(conid).ifPresent(v -> url.queryParam("conid", conid));
      Optional.ofNullable(sectype).ifPresent(v -> url.queryParam("sectype", sectype));
      Optional.ofNullable(month).ifPresent(v -> url.queryParam("month", month));
      Optional.ofNullable(exchange).ifPresent(v -> url.queryParam("exchange", exchange));
      Optional.ofNullable(strike).ifPresent(v -> url.queryParam("strike", strike));
      Optional.ofNullable(right).ifPresent(v -> url.queryParam("right", right));
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<SecdefInfo>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public Contract iserverContractConidInfoGet(String conid) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/contract/{conid}/info");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<Contract>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverAccountAccountIdOrderOrderIdPost200ResponseInner> iserverAccountAccountIdOrderOrderIdPost(
      String accountId, String orderId, ModifyOrder body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/{accountId}/order/{orderId}");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverAccountAccountIdOrderOrderIdPost200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountAccountIdAlertActivatePost200Response iserverAccountAccountIdAlertAlertIdDelete(
      String accountId, String alertId) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/:accountId/alert/:alertId");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .DELETE();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountAccountIdAlertActivatePost200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountPost200Response iserverAccountPost(SetAccount body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountPost200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverScannerParamsGet200Response iserverScannerParamsGet() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/scanner/params");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverScannerParamsGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public AuthStatus iserverReauthenticatePost() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/reauthenticate");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .POST(HttpRequest.BodyPublishers.noBody());
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<AuthStatus>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public AuthStatus iserverAuthStatusPost() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/auth/status");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .POST(HttpRequest.BodyPublishers.noBody());
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<AuthStatus>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverAccountAccountIdOrderPost200ResponseInner> iserverAccountAccountIdOrdersPost(
      String accountId, IserverAccountAccountIdOrdersPostRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/{accountId}/orders");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverAccountAccountIdOrderPost200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverSecdefStrikesGet200Response iserverSecdefStrikesGet(String conid, String sectype,
      String month, String exchange) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/secdef/strikes");
      Optional.ofNullable(conid).ifPresent(v -> url.queryParam("conid", conid));
      Optional.ofNullable(sectype).ifPresent(v -> url.queryParam("sectype", sectype));
      Optional.ofNullable(month).ifPresent(v -> url.queryParam("month", month));
      Optional.ofNullable(exchange).ifPresent(v -> url.queryParam("exchange", exchange));
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverSecdefStrikesGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public AlertResponse iserverAccountMtaGet() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/mta");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<AlertResponse>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountAccountIdAlertPost200Response iserverAccountAccountIdAlertPost(
      String accountId, AlertRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/{accountId}/alert");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountAccountIdAlertPost200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverMarketdataUnsubscribeallGet200Response iserverMarketdataUnsubscribeallGet() {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/marketdata/unsubscribeall");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverMarketdataUnsubscribeallGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverMarketdataConidUnsubscribeGet200Response iserverMarketdataConidUnsubscribeGet(
      String conid) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/marketdata/{conid}/unsubscribe");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverMarketdataConidUnsubscribeGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverSecdefSearchPost200ResponseInner> iserverSecdefSearchPost(
      IserverSecdefSearchPostRequest symbol) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/secdef/search");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(symbol)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverSecdefSearchPost200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public OrderStatus iserverAccountOrderStatusOrderIdGet(String orderId) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/order/status/{orderId}");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<OrderStatus>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public HistoryData iserverMarketdataHistoryGet(String conid, String period, String exchange,
      String bar, Boolean outsideRth) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/marketdata/history");
      Optional.ofNullable(conid).ifPresent(v -> url.queryParam("conid", conid));
      Optional.ofNullable(period).ifPresent(v -> url.queryParam("period", period));
      Optional.ofNullable(exchange).ifPresent(v -> url.queryParam("exchange", exchange));
      Optional.ofNullable(bar).ifPresent(v -> url.queryParam("bar", bar));
      Optional.ofNullable(outsideRth).ifPresent(v -> url.queryParam("outsideRth", outsideRth));
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<HistoryData>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverContractConidAlgosGet200ResponseInner> iserverContractConidAlgosGet(
      String conid, String algos, String addDescription, String addParams) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/contract/{conid}/algos");
      Optional.ofNullable(algos).ifPresent(v -> url.queryParam("algos", algos));
      Optional.ofNullable(addDescription).ifPresent(v -> url.queryParam("addDescription", addDescription));
      Optional.ofNullable(addParams).ifPresent(v -> url.queryParam("addParams", addParams));
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverContractConidAlgosGet200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverMarketdataSnapshotGet200ResponseInner> iserverMarketdataSnapshotGet(
      String conids, Integer since, String fields) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/marketdata/snapshot");
      Optional.ofNullable(conids).ifPresent(v -> url.queryParam("conids", conids));
      Optional.ofNullable(since).ifPresent(v -> url.queryParam("since", since));
      Optional.ofNullable(fields).ifPresent(v -> url.queryParam("fields", fields));
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverMarketdataSnapshotGet200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverAccountAccountIdOrderPost200ResponseInner> iserverAccountOrdersFaGroupPost(
      String faGroup, OrderRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/orders/{faGroup}");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverAccountAccountIdOrderPost200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverReplyReplyidPost200ResponseInner> iserverReplyReplyidPost(String replyid,
      IserverReplyReplyidPostRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/reply/{replyid}");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverReplyReplyidPost200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountAccountIdOrderWhatifPost200Response iserverAccountAccountIdOrderWhatifPost(
      String accountId, OrderRequest body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/{accountId}/order/whatif");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountAccountIdOrderWhatifPost200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverScannerRunPost200ResponseInner> iserverScannerRunPost(ScannerParams body) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/scanner/run");
      final var request = HttpRequest.newBuilder().uri(url.build())
            .POST(HttpRequest.BodyPublishers.ofString(jackson.writeValueAsString(body)));
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverScannerRunPost200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverContractConidInfoAndRulesGet200Response iserverContractConidInfoAndRulesGet(
      String conid, Boolean isBuy) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/contract/{conid}/info-and-rules");
      Optional.ofNullable(isBuy).ifPresent(v -> url.queryParam("isBuy", isBuy));
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverContractConidInfoAndRulesGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public IserverAccountOrdersGet200Response iserverAccountOrdersGet(String filters) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/orders");
      Optional.ofNullable(filters).ifPresent(v -> url.queryParam("Filters", filters));
      final var request = HttpRequest.newBuilder().uri(url.build())
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<IserverAccountOrdersGet200Response>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<IserverAccountAccountIdAlertsGet200ResponseInner> iserverAccountAccountIdAlertsGet(
      String accountId) {
    try {
      final var url = UriBuilder.fromUri(baseUrl).path("/iserver/account/:accountId/alerts");
      final var pathParams = new HashMap<String,Object>();
      final var request = HttpRequest.newBuilder().uri(url.buildFromMap(pathParams))
            .GET();
      final var response = httpClient.send(request.build(), new JacksonBodyHandler<List<IserverAccountAccountIdAlertsGet200ResponseInner>>(jackson));
      switch(response.statusCode()) {
        case 200: return response.body().get().getBody();
        default: throw new RuntimeException("Http Status: " + response.statusCode() + " - " + response.body().get().getText());
      }
    } catch(IOException e) {
      throw new UncheckedIOException(e);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
