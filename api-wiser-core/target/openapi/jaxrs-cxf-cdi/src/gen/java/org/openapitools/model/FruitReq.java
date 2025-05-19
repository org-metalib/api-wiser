package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.openapitools.model.AppleReq;
import org.openapitools.model.BananaReq;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class FruitReq   {
  
  private String cultivar;

  private Boolean mealy;

  private BigDecimal lengthCm;

  private Boolean sweet;

  /**
   **/
  public FruitReq cultivar(String cultivar) {
    this.cultivar = cultivar;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("cultivar")
  @NotNull
  public String getCultivar() {
    return cultivar;
  }
  public void setCultivar(String cultivar) {
    this.cultivar = cultivar;
  }


  /**
   **/
  public FruitReq mealy(Boolean mealy) {
    this.mealy = mealy;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("mealy")
  public Boolean getMealy() {
    return mealy;
  }
  public void setMealy(Boolean mealy) {
    this.mealy = mealy;
  }


  /**
   **/
  public FruitReq lengthCm(BigDecimal lengthCm) {
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
  public FruitReq sweet(Boolean sweet) {
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
    FruitReq fruitReq = (FruitReq) o;
    return Objects.equals(this.cultivar, fruitReq.cultivar) &&
        Objects.equals(this.mealy, fruitReq.mealy) &&
        Objects.equals(this.lengthCm, fruitReq.lengthCm) &&
        Objects.equals(this.sweet, fruitReq.sweet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cultivar, mealy, lengthCm, sweet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FruitReq {\n");
    
    sb.append("    cultivar: ").append(toIndentedString(cultivar)).append("\n");
    sb.append("    mealy: ").append(toIndentedString(mealy)).append("\n");
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

