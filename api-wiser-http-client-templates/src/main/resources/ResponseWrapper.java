package org.metalib.market.data.ibkr.http.client;

import lombok.Getter;

@Getter
class ResponseWrapper<T> {
  private T body;

  private String text;

  ResponseWrapper(T body) {
    this.body = body;
  }

  ResponseWrapper(byte[] content) {
    this.text = new String(content);
  }
}
