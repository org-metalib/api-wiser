package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class FakeBigDecimalMap200Response   {
  
  private BigDecimal someId;

  private Map<String, BigDecimal> someMap = new HashMap<>();

  /**
   **/
  public FakeBigDecimalMap200Response someId(BigDecimal someId) {
    this.someId = someId;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("someId")
  public BigDecimal getSomeId() {
    return someId;
  }
  public void setSomeId(BigDecimal someId) {
    this.someId = someId;
  }


  /**
   **/
  public FakeBigDecimalMap200Response someMap(Map<String, BigDecimal> someMap) {
    this.someMap = someMap;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("someMap")
  public Map<String, BigDecimal> getSomeMap() {
    return someMap;
  }
  public void setSomeMap(Map<String, BigDecimal> someMap) {
    this.someMap = someMap;
  }


  public FakeBigDecimalMap200Response putSomeMapItem(String key, BigDecimal someMapItem) {
    if (this.someMap == null) {
      this.someMap = new HashMap<>();
    }
    this.someMap.put(key, someMapItem);
    return this;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FakeBigDecimalMap200Response fakeBigDecimalMap200Response = (FakeBigDecimalMap200Response) o;
    return Objects.equals(this.someId, fakeBigDecimalMap200Response.someId) &&
        Objects.equals(this.someMap, fakeBigDecimalMap200Response.someMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(someId, someMap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FakeBigDecimalMap200Response {\n");
    
    sb.append("    someId: ").append(toIndentedString(someId)).append("\n");
    sb.append("    someMap: ").append(toIndentedString(someMap)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

