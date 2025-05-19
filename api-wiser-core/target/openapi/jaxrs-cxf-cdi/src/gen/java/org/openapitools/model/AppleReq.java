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



public class AppleReq   {
  
  private String cultivar;

  private Boolean mealy;

  /**
   **/
  public AppleReq cultivar(String cultivar) {
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
  public AppleReq mealy(Boolean mealy) {
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



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppleReq appleReq = (AppleReq) o;
    return Objects.equals(this.cultivar, appleReq.cultivar) &&
        Objects.equals(this.mealy, appleReq.mealy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cultivar, mealy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppleReq {\n");
    
    sb.append("    cultivar: ").append(toIndentedString(cultivar)).append("\n");
    sb.append("    mealy: ").append(toIndentedString(mealy)).append("\n");
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

