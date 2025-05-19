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



public class Whale   {
  
  private Boolean hasBaleen;

  private Boolean hasTeeth;

  private String className;

  /**
   **/
  public Whale hasBaleen(Boolean hasBaleen) {
    this.hasBaleen = hasBaleen;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("hasBaleen")
  public Boolean getHasBaleen() {
    return hasBaleen;
  }
  public void setHasBaleen(Boolean hasBaleen) {
    this.hasBaleen = hasBaleen;
  }


  /**
   **/
  public Whale hasTeeth(Boolean hasTeeth) {
    this.hasTeeth = hasTeeth;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("hasTeeth")
  public Boolean getHasTeeth() {
    return hasTeeth;
  }
  public void setHasTeeth(Boolean hasTeeth) {
    this.hasTeeth = hasTeeth;
  }


  /**
   **/
  public Whale className(String className) {
    this.className = className;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("className")
  @NotNull
  public String getClassName() {
    return className;
  }
  public void setClassName(String className) {
    this.className = className;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Whale whale = (Whale) o;
    return Objects.equals(this.hasBaleen, whale.hasBaleen) &&
        Objects.equals(this.hasTeeth, whale.hasTeeth) &&
        Objects.equals(this.className, whale.className);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hasBaleen, hasTeeth, className);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Whale {\n");
    
    sb.append("    hasBaleen: ").append(toIndentedString(hasBaleen)).append("\n");
    sb.append("    hasTeeth: ").append(toIndentedString(hasTeeth)).append("\n");
    sb.append("    className: ").append(toIndentedString(className)).append("\n");
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

