package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class BananaReq   {
  
  private BigDecimal lengthCm;

  private Boolean sweet;

  /**
   **/
  public BananaReq lengthCm(BigDecimal lengthCm) {
    this.lengthCm = lengthCm;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("lengthCm")
  @NotNull
  public BigDecimal getLengthCm() {
    return lengthCm;
  }
  public void setLengthCm(BigDecimal lengthCm) {
    this.lengthCm = lengthCm;
  }


  /**
   **/
  public BananaReq sweet(Boolean sweet) {
    this.sweet = sweet;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("sweet")
  public Boolean getSweet() {
    return sweet;
  }
  public void setSweet(Boolean sweet) {
    this.sweet = sweet;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BananaReq bananaReq = (BananaReq) o;
    return Objects.equals(this.lengthCm, bananaReq.lengthCm) &&
        Objects.equals(this.sweet, bananaReq.sweet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lengthCm, sweet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BananaReq {\n");
    
    sb.append("    lengthCm: ").append(toIndentedString(lengthCm)).append("\n");
    sb.append("    sweet: ").append(toIndentedString(sweet)).append("\n");
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

