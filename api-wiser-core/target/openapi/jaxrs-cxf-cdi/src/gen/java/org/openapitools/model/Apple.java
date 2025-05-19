package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class Apple   {
  
  private String cultivar;

  private String origin;

  /**
   **/
  public Apple cultivar(String cultivar) {
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
  public Apple origin(String origin) {
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



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Apple apple = (Apple) o;
    return Objects.equals(this.cultivar, apple.cultivar) &&
        Objects.equals(this.origin, apple.origin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cultivar, origin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Apple {\n");
    
    sb.append("    cultivar: ").append(toIndentedString(cultivar)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
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

