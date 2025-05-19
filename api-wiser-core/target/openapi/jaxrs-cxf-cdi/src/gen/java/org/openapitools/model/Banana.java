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



public class Banana   {
  
  private BigDecimal lengthCm;

  /**
   **/
  public Banana lengthCm(BigDecimal lengthCm) {
    this.lengthCm = lengthCm;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("lengthCm")
  public BigDecimal getLengthCm() {
    return lengthCm;
  }
  public void setLengthCm(BigDecimal lengthCm) {
    this.lengthCm = lengthCm;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Banana banana = (Banana) o;
    return Objects.equals(this.lengthCm, banana.lengthCm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lengthCm);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Banana {\n");
    
    sb.append("    lengthCm: ").append(toIndentedString(lengthCm)).append("\n");
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

