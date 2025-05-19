package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.openapitools.model.Apple;
import org.openapitools.model.Banana;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class Fruit   {
  
  private String color;

  private String cultivar;

  private String origin;

  private BigDecimal lengthCm;

  /**
   **/
  public Fruit color(String color) {
    this.color = color;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("color")
  public String getColor() {
    return color;
  }
  public void setColor(String color) {
    this.color = color;
  }


  /**
   **/
  public Fruit cultivar(String cultivar) {
    this.cultivar = cultivar;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("cultivar")
 @Pattern(regexp="^[a-zA-Z\\s]*$")  public String getCultivar() {
    return cultivar;
  }
  public void setCultivar(String cultivar) {
    this.cultivar = cultivar;
  }


  /**
   **/
  public Fruit origin(String origin) {
    this.origin = origin;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("origin")
 @Pattern(regexp="/^[A-Z\\s]*$/i")  public String getOrigin() {
    return origin;
  }
  public void setOrigin(String origin) {
    this.origin = origin;
  }


  /**
   **/
  public Fruit lengthCm(BigDecimal lengthCm) {
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
    Fruit fruit = (Fruit) o;
    return Objects.equals(this.color, fruit.color) &&
        Objects.equals(this.cultivar, fruit.cultivar) &&
        Objects.equals(this.origin, fruit.origin) &&
        Objects.equals(this.lengthCm, fruit.lengthCm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, cultivar, origin, lengthCm);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Fruit {\n");
    
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    cultivar: ").append(toIndentedString(cultivar)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
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

